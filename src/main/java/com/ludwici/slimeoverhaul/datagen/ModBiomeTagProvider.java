package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModBiomeTagProvider extends BiomeTagsProvider {
    public ModBiomeTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, provider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(AIR_SLIME_BIOME_TAG)
                .addTag(Tags.Biomes.IS_OVERWORLD)
        ;

        tag(WATER_SLIME_BIOME_TAG)
                .addTag(Tags.Biomes.IS_OVERWORLD)
        ;

        tag(EARTH_SLIME_BIOME_TAG)
                .addTag(Tags.Biomes.IS_PLAINS)
                .addTag(Tags.Biomes.IS_FOREST)
                .addTag(Tags.Biomes.IS_BADLANDS)
                .addTag(Tags.Biomes.IS_SAVANNA)
        ;

        tag(FLAME_SLIME_BIOME_TAG)
                .addTag(Tags.Biomes.IS_OVERWORLD)
                .addTag(Tags.Biomes.IS_NETHER)
        ;
    }
}
