package com.ludwici.slimeoverhaul.world;

import com.ludwici.crumbslib.api.world.feature.ConfiguredFeaturesHelper;
import com.ludwici.slimeoverhaul.block.crystallized.CrystallizedSlimeBlock;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.List;

import static com.ludwici.slimeoverhaul.Content.*;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PYROCIDE_GEODE_KEY = ConfiguredFeaturesHelper.registerKey("pyrocide_geode");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        ConfiguredFeaturesHelper.register(
                context,
                PYROCIDE_GEODE_KEY,
                Feature.GEODE,
                new GeodeConfiguration(
                        new GeodeBlockSettings(
                                BlockStateProvider.simple(Blocks.AIR),
                                BlockStateProvider.simple(Blocks.BLACKSTONE),
                                BlockStateProvider.simple(Blocks.GILDED_BLACKSTONE),
                                BlockStateProvider.simple(Blocks.MAGMA_BLOCK),
                                BlockStateProvider.simple(Blocks.SMOOTH_BASALT),
                                List.of(
                                        PYROCIDE_BLOCK.get().defaultBlockState(),
                                        PYROCIDE_BLOCK.get().defaultBlockState(),
                                        PYROCIDE_BLOCK.get().defaultBlockState()
                                ),
                                BlockTags.FEATURES_CANNOT_REPLACE,
                                BlockTags.GEODE_INVALID_BLOCKS
                        ),
                        new GeodeLayerSettings(1.3, 2, 3, 4),
                        new GeodeCrackSettings(0.75, 1.0, 1),
                        0.7,
                        0.03,
                        true,
                        UniformInt.of(3, 5),
                        UniformInt.of(3, 4),
                        UniformInt.of(1, 2),
                        -16,
                        16,
                        0.05,
                         1
                )
        );
    }
}
