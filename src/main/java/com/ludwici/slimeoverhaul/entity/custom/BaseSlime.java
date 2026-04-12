package com.ludwici.slimeoverhaul.entity.custom;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.NotImplementedException;

import java.util.EnumSet;

public class BaseSlime extends Slime {

    public BaseSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
        this.moveControl = initMoveControl();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, getFloatGoal());
        this.goalSelector.addGoal(2, getAttackGoal());
        this.goalSelector.addGoal(3, getDirectionGoal());
        this.goalSelector.addGoal(5, getJumpGoal());
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (livingEntity, level) -> Math.abs(((LivingEntity)livingEntity).getY() - this.getY()) <= (double)4.0F));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    protected MoveControl initMoveControl() {
        return new SlimeMoveControl<>(this);
    }

    protected Goal getFloatGoal () {
        return new SlimeFloatGoal(this);
    }

    protected Goal getAttackGoal() {
        return new SlimeAttackGoal(this);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        return super.canAttack(target);
    }

    protected Goal getDirectionGoal() {
        return new SlimeRandomDirectionGoal(this);
    }

    protected Goal getJumpGoal() {
        return new SlimeKeepOnJumpingGoal(this);
    }

    public static AttributeSupplier.Builder createMobAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10d)
                .add(Attributes.MOVEMENT_SPEED, 0.2f)
                .add(Attributes.ATTACK_DAMAGE, 0f);
    }

    public String getSlimeType() {
        throw new NotImplementedException ("You should set slime type for getting valid texture in renderer class");
    }

    @Override
    protected void dealDamage(LivingEntity livingEntity) {
        super.dealDamage(livingEntity);
    }

    @Override
    protected boolean isDealsDamage() {
        return this.isEffectiveAi();
    }

    @Override
    public ParticleOptions getParticleType() {
        return super.getParticleType();
    }

    @Override
    public float getSoundVolume() {
        return super.getSoundVolume();
    }

    @Override
    public SoundEvent getSquishSound() {
        return super.getSquishSound();
    }

    @Override
    public int getJumpDelay() {
        return super.getJumpDelay();
    }

    public boolean checkAdditionalGoals() {
        return true;
    }

    protected float getSoundPitch() {
        float f = this.isTiny() ? 1.4F : 0.8F;
        return ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * f;
    }

    protected static class SlimeMoveControl <T extends BaseSlime> extends MoveControl {
        protected float yRot;
        protected int jumpDelay;
        protected final T slime;
        protected boolean isAggressive;

        public SlimeMoveControl(T slime) {
            super(slime);
            this.slime = slime;
            this.yRot = 180.0F * slime.getYRot() / (float)Math.PI;
        }

        @Override
        public void setWantedPosition(double d, double e, double f, double g) {
            super.setWantedPosition(d, e, f, g);
        }

        public void setDirection(float f, boolean bl) {
            this.yRot = f;
            this.isAggressive = bl;
        }

        public void setWantedMovement(double d) {
            this.speedModifier = d;
            this.operation = Operation.MOVE_TO;
        }

        public void onJump() {
            this.slime.getJumpControl().jump();
            if (this.slime.doPlayJumpSound()) {
                this.slime.playSound(this.slime.getJumpSound(), this.slime.getSoundVolume(), this.slime.getSoundPitch());
            }
        }

        public void onFall() {

        }

        public void tick() {
            this.mob.setYRot(this.rotlerp(this.mob.getYRot(), this.yRot, 90.0F));
            this.mob.yHeadRot = this.mob.getYRot();
            this.mob.yBodyRot = this.mob.getYRot();
            if (this.operation != Operation.MOVE_TO) {
                this.mob.setZza(0.0F);
            } else {
                this.operation = Operation.WAIT;
                if (this.mob.onGround()) {
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    if (this.jumpDelay-- <= 0) {
                        this.jumpDelay = this.slime.getJumpDelay();
                        if (this.isAggressive) {
                            this.jumpDelay /= 3;
                        }
                        onJump();
                    } else {
                        this.slime.xxa = 0.0F;
                        this.slime.zza = 0.0F;
                        this.mob.setSpeed(0.0F);
                    }
                } else {
                    onFall();
                    this.mob.setSpeed((float)(this.speedModifier * this.mob.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                }
            }
        }
    }

    protected static class SlimeKeepOnJumpingGoal extends Goal {
        private final BaseSlime slime;

        public SlimeKeepOnJumpingGoal(BaseSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.JUMP, Flag.MOVE));
        }

        public boolean canUse() {
            return !this.slime.isPassenger();
        }

        public void tick() {
            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement((double)1.0F);
            }
        }
    }

    protected static class SlimeRandomDirectionGoal extends Goal {
        private final BaseSlime slime;
        private float chosenDegrees;
        private int nextRandomizeTime;

        public SlimeRandomDirectionGoal(BaseSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            return this.slime.getTarget() == null && this.slime.checkAdditionalGoals() && (this.slime.onGround() || this.slime.isInWater() || this.slime.isInLava() || this.slime.hasEffect(MobEffects.LEVITATION)) && this.slime.getMoveControl() instanceof SlimeMoveControl;
        }

        public void tick() {
            if (--this.nextRandomizeTime <= 0) {
                this.nextRandomizeTime = this.adjustedTickDelay(40 + this.slime.getRandom().nextInt(60));
                this.chosenDegrees = (float)this.slime.getRandom().nextInt(360);
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(this.chosenDegrees, false);
            }
        }
    }

    protected static class SlimeAttackGoal extends Goal {
        private final BaseSlime slime;
        private int growTiredTimer;

        public SlimeAttackGoal(BaseSlime slime) {
            this.slime = slime;
            this.setFlags(EnumSet.of(Flag.LOOK));
        }

        public boolean canUse() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else {
                return !this.slime.canAttack(livingEntity) ? false : this.slime.getMoveControl() instanceof SlimeMoveControl;
            }
        }

        public void start() {
            this.growTiredTimer = reducedTickDelay(300);
            super.start();
        }

        public boolean canContinueToUse() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!this.slime.canAttack(livingEntity)) {
                return false;
            } else {
                return --this.growTiredTimer > 0;
            }
        }

        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tick() {
            LivingEntity livingEntity = this.slime.getTarget();
            if (livingEntity != null) {
                this.slime.lookAt(livingEntity, 10.0F, 10.0F);
            }

            MoveControl var3 = this.slime.getMoveControl();
            if (var3 instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setDirection(this.slime.getYRot(), this.slime.isDealsDamage());
            }
        }
    }

    protected static class SlimeFloatGoal extends Goal {
        private final BaseSlime slime;

        public SlimeFloatGoal(BaseSlime slime) {
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
            if (this.slime.getRandom().nextFloat() < 0.8F) {
                this.slime.getJumpControl().jump();
            }

            MoveControl var2 = this.slime.getMoveControl();
            if (var2 instanceof SlimeMoveControl slimeMoveControl) {
                slimeMoveControl.setWantedMovement(1.2);
            }

        }
    }

}
