package com.ludwici.slimeoverhaul.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;

public class AntiDepthEffect extends MobEffect {
    private Fluid fluid;

    public AntiDepthEffect(MobEffectCategory mobEffectCategory, Fluid fluidType, int color) {
        super(mobEffectCategory, color);
        this.fluid = fluidType;
    }

    @Override
    public boolean applyEffectTick(LivingEntity livingEntity, int i) {
        if (checkLiquid(livingEntity)) {
            if (livingEntity instanceof Player player) {
                if (player.isSpectator()) {
                    return true;
                }
            }
            Vec3 initialVec = livingEntity.getDeltaMovement();
            double value = initialVec.y;
            value += (0.2 * (double)(4) - initialVec.y) * 0.4;
            livingEntity.setDeltaMovement(initialVec.x, value, initialVec.z);
        }

        return true;
    }

    protected boolean checkLiquid(LivingEntity livingEntity) {
        return livingEntity.getEyeInFluidType() == fluid.getFluidType();
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int i, int j) {
        return true;
    }
}
