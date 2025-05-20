package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;

public class ThunderSlime extends BaseSlime {
    BlockPos anchorPoint = BlockPos.ZERO;

    public ThunderSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public String getSlimeType() {
        return "thunder";
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.ELECTRIC_SPARK;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(5, getFloatGoal());
        this.goalSelector.addGoal(7, getAttackGoal());
        this.goalSelector.addGoal(7, getDirectionGoal());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, livingEntity -> Math.abs(livingEntity.getY() - this.getY()) <= 4.0F));
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return BaseSlime.createMobAttributes()
                .add(Attributes.FOLLOW_RANGE, 100);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnType, @Nullable SpawnGroupData spawnGroupData) {
        this.anchorPoint = this.blockPosition().above(5);
        return super.finalizeSpawn(level, difficulty, spawnType, spawnGroupData);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("AX")) {
            anchorPoint = new BlockPos(
                    compound.getInt("AX"),
                    compound.getInt("AY"),
                    compound.getInt("AZ")
            );
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AX", anchorPoint.getX());
        compound.putInt("AY", anchorPoint.getY());
        compound.putInt("AZ", anchorPoint.getZ());
    }

    @Override
    protected MoveControl initMoveControl() {
        return new ThunderSlimeMoveControl(this);
    }

    @Override
    protected Goal getDirectionGoal() {
        return new SlimeRandomDirectionGoal(this);
    }

    @Override
    protected Goal getFloatGoal() {
        return new SlimeFloatGoal(this);
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (this.isControlledByLocalInstance()) {
            if (this.isInWater()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.8F));
            } else if (this.isInLava()) {
                this.moveRelative(0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
            } else {
                BlockPos ground = getBlockPosBelowThatAffectsMyMovement();
                float f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                float f1 = 0.16277137F / (f * f * f);
                f = 0.91F;
                if (this.onGround()) {
                    f = this.level().getBlockState(ground).getFriction(this.level(), ground, this) * 0.91F;
                }

                this.moveRelative(this.onGround() ? 0.1F * f1 : 0.02F, travelVector);
                this.move(MoverType.SELF, this.getDeltaMovement());
                this.setDeltaMovement(this.getDeltaMovement().scale((double)f));
            }
        }

        this.calculateEntityAnimation(false);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {
            return true;
        }

        if (source.is(DamageTypes.WIND_CHARGE)) {
            return true;
        }

        return super.isInvulnerableTo(source);
    }

    protected static class ThunderSlimeMoveControl extends MoveControl {
        private final ThunderSlime slime;
        private int floatDuration;

        public ThunderSlimeMoveControl(ThunderSlime mob) {
            super(mob);
            this.slime = mob;
        }

        @Override
        public void tick() {
            if (this.operation == Operation.MOVE_TO) {
                if (floatDuration-- <= 0) {
                    floatDuration = floatDuration + slime.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(wantedX - slime.getX(), wantedY - slime.getY(), wantedZ - slime.getZ());
                    double d0 = vec3.length();
                    vec3 = vec3.normalize();
                    if (canReach(vec3, Mth.ceil(d0))) {
                        slime.setDeltaMovement(slime.getDeltaMovement().add(vec3.scale(0.1)));
                    } else {
                        operation = Operation.WAIT;
                    }
                }
            }
        }

        private boolean canReach(Vec3 pos, int length) {
            AABB aabb = slime.getBoundingBox();

            for (int i = 1; i < length; i++) {
                aabb = aabb.move(pos);
                if (!slime.level().noCollision(slime, aabb)) {
                    return false;
                }
            }

            return true;
        }
    }

    protected static class SlimeRandomDirectionGoal extends Goal {
        private final ThunderSlime slime;

        public SlimeRandomDirectionGoal(ThunderSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return slime.checkAdditionalGoals() && slime.getMoveControl() instanceof ThunderSlimeMoveControl;
        }

        @Override
        public void tick() {
            if (slime.getTarget() == null) {
                Vec3 vec3 = slime.getDeltaMovement();
                slime.setYRot(-((float)Mth.atan2(vec3.x, vec3.z)) * (180.0F / (float)Math.PI));
                slime.yBodyRot = slime.getYRot();
            } else {
                LivingEntity livingentity = slime.getTarget();
                double d0 = 64.0;
                if (livingentity.distanceToSqr(slime) < 4096.0) {
                    double d1 = livingentity.getX() - slime.getX();
                    double d2 = livingentity.getZ() - slime.getZ();
                    slime.setYRot(-((float)Mth.atan2(d1, d2)) * (180.0F / (float)Math.PI));
                    slime.yBodyRot = slime.getYRot();
                }
            }
        }
    }

    protected static class SlimeFloatGoal extends Goal {
        protected final ThunderSlime slime;
        protected float distance;

        public SlimeFloatGoal(ThunderSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl movecontrol = slime.getMoveControl();
            if (!movecontrol.hasWanted()) {
                return true;
            } else {
                double d0 = movecontrol.getWantedX() - slime.getX();
                double d1 = movecontrol.getWantedY() - slime.getY();
                double d2 = movecontrol.getWantedZ() - slime.getZ();
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0 || d3 > 3600.0;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            RandomSource randomsource = slime.getRandom();
            distance = 5.0f + randomsource.nextFloat() * 10.0f;
            double d0 = slime.getX() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = slime.getY() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = slime.getZ() + (double)((randomsource.nextFloat() * 2.0F - 1.0F) * 16.0F);
            slime.getMoveControl().setWantedPosition(d0, d1, d2, 1.0);
            selectNext();
        }

        @Override
        public void tick() {
            if (slime.random.nextInt(adjustedTickDelay(250)) == 0) {
                distance++;
                if (distance > 15.0f) {
                    distance = 5.0f;
                }
            }
            if (!slime.anchorPoint.closerThan(slime.blockPosition(), distance)) {
                slime.getMoveControl().setWantedPosition(
                        slime.anchorPoint.getX(),
                        slime.anchorPoint.getY(),
                        slime.anchorPoint.getZ(),
                        1.0
                );
            }
            System.out.println(slime.blockPosition() + "|" + slime.anchorPoint + "|" + distance);
        }

        protected void selectNext() {
            if (BlockPos.ZERO.equals(slime.anchorPoint)) {
                slime.anchorPoint = slime.blockPosition();
            }
        }
    }
}
