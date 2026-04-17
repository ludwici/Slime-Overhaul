package com.ludwici.slimeoverhaul.world;

import com.ludwici.crumbslib.api.world.feature.PlacedFeaturesHelper;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class ModPacedFeatures {
    public static final ResourceKey<PlacedFeature> PYROCIDE_GEODE_PLACED_KEY = PlacedFeaturesHelper.registerKey("pyrocide_geode_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder<ConfiguredFeature<?, ?>> geodeFuture = configuredFeature.getOrThrow(ModConfiguredFeatures.PYROCIDE_GEODE_KEY);

        PlacedFeaturesHelper.register(
                context,
                PYROCIDE_GEODE_PLACED_KEY,
                geodeFuture,
                List.of(
                        RarityFilter.onAverageOnceEvery(53),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(6), VerticalAnchor.absolute(30)),
                        BiomeFilter.biome()
                )
        );
    }
}
