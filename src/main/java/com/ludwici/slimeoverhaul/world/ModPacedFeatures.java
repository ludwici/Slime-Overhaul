package com.ludwici.slimeoverhaul.world;

import com.ludwici.crumbslib.api.world.feature.PacedFeaturesHelper;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPacedFeatures {
    public static final ResourceKey<PlacedFeature> FIRE_CRYSTALLIZED_SLIME_PLACED_KEY = PacedFeaturesHelper.registerKey("fire_crystallized_slime_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);

        PacedFeaturesHelper.register(context, FIRE_CRYSTALLIZED_SLIME_PLACED_KEY, configuredFeature.getOrThrow(
                        ModConfiguredFeatures.OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY),
                List.of(
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome(),
                        CountPlacement.of(10)
                )
        );
    }
}
