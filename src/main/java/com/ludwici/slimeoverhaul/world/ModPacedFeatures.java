package com.ludwici.slimeoverhaul.world;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModPacedFeatures {
    public static final ResourceKey<PlacedFeature> FIRE_CRYSTALLIZED_SLIME_PLACED_KEY = registerKey("fire_crystallized_slime_placed");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeature = context.lookup(Registries.CONFIGURED_FEATURE);

        register(context, FIRE_CRYSTALLIZED_SLIME_PLACED_KEY, configuredFeature.getOrThrow(
                        ModConfiguredFeatures.OVERWORLD_FIRE_CRYSTALLIZED_SLIME_KEY),
                List.of(
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(320)),
                        InSquarePlacement.spread(),
                        BiomeFilter.biome(),
                        CountPlacement.of(10)
                )
        );
    }

    private static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(MODID, name));
    }

    private static void register(BootstrapContext<PlacedFeature> context, ResourceKey<PlacedFeature> key, Holder<ConfiguredFeature<?, ?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }
}
