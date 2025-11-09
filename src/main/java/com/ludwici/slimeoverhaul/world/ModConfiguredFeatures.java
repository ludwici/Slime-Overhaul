package com.ludwici.slimeoverhaul.world;

import com.ludwici.crumbslib.api.world.feature.ConfiguredFeaturesHelper;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.ludwici.slimeoverhaul.Content.FIRE_CRYSTALLIZED_SLIME_FEATURE;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY = ConfiguredFeaturesHelper.registerKey("fire_crystallized_slime");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ConfiguredFeaturesHelper.register(context, OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY, FIRE_CRYSTALLIZED_SLIME_FEATURE.get(), NoneFeatureConfiguration.INSTANCE);
    }
}
