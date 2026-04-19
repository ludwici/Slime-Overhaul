package com.ludwici.slimeoverhaul.datamap;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.ItemStack;

public record UpgradeCost(ItemStack itemStack) {
    public static final Codec<UpgradeCost> CODEC = RecordCodecBuilder.create(instance -> instance.group(
//            Codec.INT.fieldOf("amount").forGetter(UpgradeCost::count)
            ItemStack.CODEC.fieldOf("itemStack").forGetter(UpgradeCost::itemStack)
    ).apply(instance, UpgradeCost::new));
}
