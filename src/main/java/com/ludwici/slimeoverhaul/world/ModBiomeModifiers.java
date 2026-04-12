package com.ludwici.slimeoverhaul.world;

import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;
import static com.ludwici.slimeoverhaul.world.ModPacedFeatures.FIRE_CRYSTALLIZED_SLIME_PLACED_KEY;

public class ModBiomeModifiers {
    public static final ResourceKey<BiomeModifier> ADD_FIRE_CRYSTALLIZED_SLIME = registerKey("add_fire_crystallized_slime");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        var biomes = context.lookup(Registries.BIOME);

        context.register(
                ADD_FIRE_CRYSTALLIZED_SLIME,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OVERWORLD),
                        HolderSet.direct(placedFeatures.getOrThrow(FIRE_CRYSTALLIZED_SLIME_PLACED_KEY)),
                        GenerationStep.Decoration.UNDERGROUND_DECORATION
                )
        );
    }

    private static ResourceKey<BiomeModifier> registerKey(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, Identifier.fromNamespaceAndPath(MODID, name));
    }
}
