package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.Tags;

import static com.ludwici.slimeoverhaul.Content.*;


public class FlameSlime extends BaseSlime implements Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET;

    public FlameSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public boolean isSensitiveToWater() {
        return true;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FROM_BUCKET, false);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("FromBucket", this.fromBucket());
    }


    @Override
    public void push(Entity entity) {
        super.push(entity);
        if (entity instanceof WaterSlime && this.isDealsDamage()) {
            this.dealDamage((LivingEntity) entity);
        }
    }

    @Override
    public void readAdditionalSaveData(ValueInput compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setFromBucket(compoundTag.getBooleanOr("FromBucket", false));
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, WaterSlime.class, true));
    }

    @Override
    public boolean isOnFire() {
        return false;
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.FLAME;
    }

    @Override
    public String getSlimeType() {
        return "flame";
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
            compoundTag.putInt("Size", this.getSize() - 1);
        });
    }

    @Override
    public void loadFromBucketTag(CompoundTag compoundTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, compoundTag);
        this.setSize(compoundTag.getInt("Size").get() + 1, false);
    }

    @Override
    public ItemStack getBucketItemStack() {
        return new ItemStack(FIRE_SLIME_BUCKET.get());
    }

    @Override
    public SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_EMPTY_AXOLOTL;
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (isAlive() && isTiny() && itemStack.getItem() == Items.LAVA_BUCKET) {
            playSound(getPickupSound(), 1.0F, 1.0F);
            ItemStack itemStack2 = getBucketItemStack();
            saveToBucketTag(itemStack2);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(interactionHand, itemStack3);
            Level level = level();
            if (!level.isClientSide()) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) player, itemStack2);
            }
            discard();
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, interactionHand);
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    public static boolean checkSpawnRules(EntityType<FlameSlime> type, LevelAccessor level, EntitySpawnReason mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (Config.SPAWN_FLAME_SLIMES.isFalse()) {
            return false;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        Holder<Biome> biome = level.getBiome(blockPos);

        if (biome.is(Tags.Biomes.IS_OVERWORLD)) {
            if (level.getFluidState(blockPos).is(FluidTags.LAVA)) {
                if (level.getBlockState(blockPos.above()).isAir() || level.getFluidState(blockPos.above()).is(FluidTags.LAVA)) {
//                    System.out.println("/tp Dev " + blockPos.getX() + " " + blockPos.getY() + " " + blockPos.getZ());
                    return true;
                }
            }
        } else if (biome.is(Tags.Biomes.IS_NETHER)) {
            if (!biome.is(Biomes.CRIMSON_FOREST)) {
                return false;
            }

            if (level.getBlockState(blockPos.below()).isAir()) {
                return false;
            }

            if (level.getFluidState(blockPos).is(FluidTags.LAVA)) {
                return false;
            }

            if (level.getRandom().nextFloat() <= 0.1F) {
                return true;
            }
        }

        return false;
    }

    static {
        FROM_BUCKET = SynchedEntityData.defineId(FlameSlime.class, EntityDataSerializers.BOOLEAN);
    }
}
