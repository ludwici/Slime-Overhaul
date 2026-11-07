package com.ludwici.slimeoverhaul.datamap;

import net.minecraft.world.item.ItemStack;

import static com.ludwici.slimeoverhaul.Content.FIRE_SLIME_BALL;
import static com.ludwici.slimeoverhaul.Content.FIRE_TEMPLATE;

public final class UpgradeCosts {
    private UpgradeCosts() {
    }

    public static UpgradeCost get(ItemStack template) {
        if (template.is(FIRE_TEMPLATE.get())) {
            return new UpgradeCost(new ItemStack(FIRE_SLIME_BALL.get()));
        }
        return null;
    }
}
