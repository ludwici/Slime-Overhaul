package com.ludwici.slimeoverhaul.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class HandBurnEffect extends MobEffect {
    public HandBurnEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    public static int getDuration(int amplifier) {
        if (amplifier == 0) {
            return 1800;
        }

        return 3600;
    }

    public static int getDamage(int amplifier) {
        return switch (amplifier) {
            case 0 -> 4;
            case 1 -> 6;
            default -> 8;
        };
    }
}
