package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
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
                .addTag(BiomeTags.IS_OVERWORLD)
        ;

        tag(WATER_SLIME_BIOME_TAG)
                .addTag(BiomeTags.IS_OVERWORLD)
        ;

        tag(EARTH_SLIME_BIOME_TAG)
                .addTag(BiomeTags.IS_OVERWORLD)
        ;

        tag(FLAME_SLIME_BIOME_TAG)
                .addTag(BiomeTags.IS_OVERWORLD)
                .addTag(BiomeTags.IS_NETHER)
        ;
    }
}
