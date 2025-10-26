package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import com.ludwici.slimeoverhaul.entity.custom.variants.WaterSlimeVariant;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

import static com.ludwici.slimeoverhaul.Content.WATER_SLIME_BUCKET;

public class WaterSlime extends BaseSlime implements Bucketable {

    public BlockPos fireBlockPos;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;
    private static final EntityDataAccessor<Integer> VARIANT;

    public WaterSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(2, new MoveToFireGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, FlameSlime.class, true));
    }

    @Override
    protected Goal getFloatGoal() {
        return new SlimeWaterMoveGoal(this);
    }

    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (entity instanceof FlameSlime && this.isDealsDamage()) {
            this.dealDamage((LivingEntity) entity);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
        builder.define(VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant().getId());
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(WaterSlimeVariant.byId(compoundTag.getInt("Variant")));
        this.setFromBucket(compoundTag.getBoolean("FromBucket"));
    }

    public WaterSlimeVariant getVariant() {
        return WaterSlimeVariant.byId((Integer) this.entityData.get(VARIANT));
    }

    public void setVariant(WaterSlimeVariant variant) {
        this.entityData.set(VARIANT, variant.getId());
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean bl) {
        this.entityData.set(FROM_BUCKET, bl);
    }

    @Override
    public void saveToBucketTag(ItemStack itemStack) {
        Bucketable.saveDefaultDataToBucketTag(this, itemStack);
        CustomData.update(DataComponents.BUCKET_ENTITY_DATA, itemStack, compoundTag -> {
            compoundTag.putInt("Variant", this.getVariant().getId());
            compoundTag.putInt("Size", this.getSize() - 1);
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
        this.setVariant(WaterSlimeVariant.byId(compoundTag.getInt("Variant")));
        this.setSize(compoundTag.getInt("Size") + 1, false);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(WATER_SLIME_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_EMPTY_AXOLOTL;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData) {
        if (mobSpawnType == MobSpawnType.BUCKET) {
            return spawnGroupData;
        }

        WaterSlimeVariant variant = Util.getRandom(WaterSlimeVariant.values(), this.random);
        this.setVariant(variant);
        if (serverLevelAccessor.getBiome(this.blockPosition()).is(BiomeTags.IS_OCEAN)) {
            this.setVariant(WaterSlimeVariant.OCEAN);
        } else {
            this.setVariant(WaterSlimeVariant.RIVER);
        }
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData);
    }

    static {
        FROM_BUCKET = SynchedEntityData.defineId(WaterSlime.class, EntityDataSerializers.BOOLEAN);
        VARIANT = SynchedEntityData.defineId(WaterSlime.class, EntityDataSerializers.INT);
    }

    class MoveToFireGoal extends Goal {

        WaterSlime slime;
        private int nextRandomizeTime;

        public MoveToFireGoal(WaterSlime mob) {
            this.slime = mob;
        }

        @Override
        public boolean canUse() {
            if (isTiny()) {
                return false;
            }
            this.slime.fireBlockPos = findFireBlock(this.slime.blockPosition(), 10);
            return this.slime.fireBlockPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return fireBlockPos != null && level().getBlockState(fireBlockPos).is(Blocks.FIRE);
        }

        private BlockPos findFireBlock(BlockPos origin, int radius) {
            int searchRange = 10;
            int verticalSearchRange = 2;
            int i = searchRange;
            int j = verticalSearchRange;

            BlockPos blockPos = origin;
            BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();

            for (int k = 0; k <= j; k = k > 0 ? -k : 1 - k) {
                for (int l = 0; l < i; ++l) {
                    for (int m = 0; m <= l; m = m > 0 ? -m : 1 - m) {
                        for (int n = m < l && m > -l ? l : 0; n <= l; n = n > 0 ? -n : 1 - n) {
                            mutableBlockPos.setWithOffset(blockPos, m, k - 1, n);
                            if (this.slime.isWithinRestriction(mutableBlockPos) && this.slime.level().getBlockState(mutableBlockPos).is(Blocks.FIRE)) {
                                return mutableBlockPos;
                            }
                        }
                    }
                }
            }
            return null;
        }

        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = Config.WATER_SLIME_FIND_FIRE.get();

                MoveControl var2 = this.slime.getMoveControl();
                if (var2 instanceof SlimeMoveControl slimeMoveControl) {
                    double dx = this.slime.fireBlockPos.getX() + 0.5 - this.slime.getX();
                    double dz = this.slime.fireBlockPos.getZ() + 0.5 - this.slime.getZ();
                    float yaw = (float) (Mth.atan2(dz, dx) * (180F / Math.PI)) - 90F;
                    slimeMoveControl.setDirection(yaw, true);
                }
            }
        }
    }

    @Override
    public boolean checkAdditionalGoals() {
        return fireBlockPos == null || !level().getBlockState(fireBlockPos).is(Blocks.FIRE);
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.FALLING_WATER;
    }

    @Override
    public String getSlimeType() {
        return "water_" + getVariant().getSerializedName();
    }

    @Override
    protected InteractionResult mobInteract(Player arg, InteractionHand arg2) {
        ItemStack itemStack = arg.getItemInHand(arg2);
        if (itemStack.is(Items.BUCKET) && getSize() == MAX_NATURAL_SIZE) {
            ItemStack itemStack2 = ItemUtils.createFilledResult(itemStack, arg, Items.WATER_BUCKET.getDefaultInstance());
            arg.setItemInHand(arg2, itemStack2);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (isTiny()) {
            return Bucketable.bucketMobPickup(arg, arg2, this).orElse(super.mobInteract(arg, arg2));
        } else {
            return super.mobInteract(arg, arg2);
        }
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public static boolean checkSpawnRules(EntityType<WaterSlime> type, LevelAccessor level, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (Config.SPAWN_WATER_SLIMES.isFalse()) {
            return false;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        Holder<Biome> biome = level.getBiome(blockPos);

        if (biome.is(BiomeTags.IS_OCEAN)) {
            int i = level.getSeaLevel();
            int j = i - 21;
            if (blockPos.getY() >= j && blockPos.getY() < i) {
                if (level.getRandom().nextFloat() <= 0.07F) {
                    return true;
                }
            }
        } else if (biome.is(BiomeTags.IS_RIVER)) {
            if (biome.is(Tags.Biomes.IS_COLD)) {
                return false;
            }
            if (level.getRandom().nextFloat() < 0.095F) {
                return true;
            }
        } else {
            if (level.getFluidState(blockPos).is(FluidTags.WATER)) {
                if (level.getRandom().nextFloat() <= 0.1F) {
//                    System.out.println("/tp Dev " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
                    return true;
                }
            }
        }

        return false;
    }

    static class SlimeWaterMoveGoal extends Goal {
        private final WaterSlime slime;
        private int nextRandomizeTime;
        private boolean toUp = false;

        public SlimeWaterMoveGoal(WaterSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
            slime.getNavigation().setCanFloat(true);
        }

        public boolean canUse() {
            return (this.slime.isInWater() || this.slime.isInLava()) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            if (--nextRandomizeTime <= 0) {
                float chance = this.slime.getRandom().nextFloat();
                toUp = this.slime.getVariant() == WaterSlimeVariant.RIVER ? chance >= 0.7F : chance >= 0.85F;
                if (toUp) {
                    this.nextRandomizeTime = this.adjustedTickDelay(10 + this.slime.getRandom().nextInt(40));
                } else {
                    this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                }
            }

            if (toUp) {
                this.slime.getJumpControl().jump();
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement(1.4);
            }

        }
    }

}
