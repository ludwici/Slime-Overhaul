package com.ludwici.slimeoverhaul.world;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.ludwici.slimeoverhaul.Content.FIRE_CRYSTALLIZED_SLIME_FEATURE;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY = registerKey("fire_crystallized_slime");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        register(context, OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY, FIRE_CRYSTALLIZED_SLIME_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(
            BootstrapContext<ConfiguredFeature<?, ?>> context,
            ResourceKey<ConfiguredFeature<?, ?>> key,
            F feature, FC configuration
    ) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
