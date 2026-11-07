package com.ludwici.slimeoverhaul.compat;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public final class Tags {
    private Tags() {
    }

    public static final class Biomes {
        public static final TagKey<Biome> IS_MOUNTAIN_PEAK = tag("is_mountain/peak");
        public static final TagKey<Biome> IS_MOUNTAIN_SLOPE = tag("is_mountain/slope");
        public static final TagKey<Biome> IS_COLD = tag("is_cold");
        public static final TagKey<Biome> IS_BADLANDS = tag("is_badlands");
        public static final TagKey<Biome> IS_OVERWORLD = tag("is_overworld");
        public static final TagKey<Biome> IS_NETHER = tag("is_nether");

        private static TagKey<Biome> tag(String path) {
            return TagKey.create(Registries.BIOME, ResourceLocation.fromNamespaceAndPath("c", path));
        }
    }

    public static final class Items {
        public static final TagKey<Item> TOOLS_SHIELD = TagKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("c", "tools/shield"));
    }
}
