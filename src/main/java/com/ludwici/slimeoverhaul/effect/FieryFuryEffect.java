package com.ludwici.slimeoverhaul.effect;

import com.ludwici.crumbslib.api.CrumbSupplier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

import static com.ludwici.slimeoverhaul.Content.*;

public class FieryFuryEffect extends MobEffect {
    public FieryFuryEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static boolean hasEffect(LivingEntity entity) {
        return entity.hasEffect(FIERY_FURY_EFFECT_I.getHolder()) || entity.hasEffect(FIERY_FURY_EFFECT_II.getHolder()) || entity.hasEffect(FIERY_FURY_EFFECT_III.getHolder());
    }

    public static CrumbSupplier<MobEffect> getEffect(LivingEntity entity) {
        if (entity.hasEffect(FIERY_FURY_EFFECT_I.getHolder())) {
            return FIERY_FURY_EFFECT_I;
        }

        if (entity.hasEffect(FIERY_FURY_EFFECT_II.getHolder())) {
            return FIERY_FURY_EFFECT_II;
        }

        if (entity.hasEffect(FIERY_FURY_EFFECT_III.getHolder())) {
            return FIERY_FURY_EFFECT_III;
        }

        return null;
    }

    public static CrumbSupplier<MobEffect> getNext(LivingEntity entity) {
        if (entity.hasEffect(FIERY_FURY_EFFECT_I.getHolder())) {
            return FIERY_FURY_EFFECT_II;
        }

        if (entity.hasEffect(FIERY_FURY_EFFECT_II.getHolder())) {
            return FIERY_FURY_EFFECT_III;
        }

        return null;
    }

    public static void removeEffect(LivingEntity entity) {
        if (entity.hasEffect(FIERY_FURY_EFFECT_I.getHolder())) {
            entity.removeEffect(FIERY_FURY_EFFECT_I.getHolder());
        }

        if (entity.hasEffect(FIERY_FURY_EFFECT_II.getHolder())) {
            entity.removeEffect(FIERY_FURY_EFFECT_II.getHolder());
        }

        if (entity.hasEffect(FIERY_FURY_EFFECT_III.getHolder())) {
            entity.removeEffect(FIERY_FURY_EFFECT_III.getHolder());
        }
    }
}
