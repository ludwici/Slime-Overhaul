package com.ludwici.slimeoverhaul.entity.custom.elementals;

import com.ludwici.slimeoverhaul.config.Config;
import com.ludwici.slimeoverhaul.entity.custom.BaseSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;

public class AirSlime extends BaseSlime {
    public boolean doubleJumped = false;

    public AirSlime(EntityType<? extends Slime> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public String getSlimeType() {
        return "air";
    }

    @Override
    public ParticleOptions getParticleType() {
        return ParticleTypes.CLOUD;
    }

    @Override
    protected float getJumpPower() {
        return 0.42F * 1.8F * this.getBlockJumpFactor() + this.getJumpBoostPower();
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    protected MoveControl initMoveControl() {
        return new AirSlimeMoveControl(this);
    }

    public static boolean checkSpawnRules(EntityType<AirSlime> type, LevelAccessor level, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        if (!Config.SPAWN_AIR_SLIMES.get()) {
            return false;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) {
            return false;
        }

        if (level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(blockPos) < 8) {
            return false;
        }

        if (level.getBlockState(blockPos.below()).isAir()) {
            return false;
        }

        Holder<Biome> biome = level.getBiome(blockPos);

        if (biome.is(Tags.Biomes.IS_PEAK)) {
            if (level.getRandom().nextFloat() <= 0.6F) {
                return true;
            }
        } else if (biome.is(Tags.Biomes.IS_SLOPE)) {
            if (blockPos.getY() >= 140) {
                if (level.getRandom().nextFloat() <= 0.35F) {
                    return true;
                }
            }
        } else {
            if (blockPos.getY() >= 160) {
                if (level.getRandom().nextFloat() <= 0.4F) {
                    return true;
                }
            }
        }

        return false;
    }


    protected static class AirSlimeMoveControl extends SlimeMoveControl<AirSlime> {
        private int nextDoubleJumpTime;

        public AirSlimeMoveControl(AirSlime slime) {
            super(slime);
        }

        @Override
        public void onFall() {
            super.onFall();
            if (this.slime.hasEffect(MobEffects.LEVITATION)) {
                return;
            }

            if (--nextDoubleJumpTime <= 0) {
                this.slime.doubleJumped = this.mob.getRandom().nextFloat() >= 0.5F;

                if (this.slime.doubleJumped) {
                    nextDoubleJumpTime = this.mob.getRandom().nextInt(100);
                } else {
                    nextDoubleJumpTime = this.mob.getRandom().nextInt(20);
                }
            }

            if (this.slime.doubleJumped) {
                this.slime.jumpFromGround();
                this.slime.doubleJumped = false;
            }
        }
    }
}
