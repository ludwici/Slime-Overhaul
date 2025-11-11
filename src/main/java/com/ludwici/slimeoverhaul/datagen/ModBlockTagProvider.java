package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        tag(SLIME_BLOCK_ITEMS)
                .add(AIR_SLIME_BLOCK.get())
                .add(WATER_SLIME_BLOCK.get())
                .add(EARTH_SLIME_BLOCK.get())
                .add(FIRE_SLIME_BLOCK.get())
        ;

        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(FIRE_CRYSTALLIZED_SLIME_BLOCK.get())
        ;

        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(FIRE_CRYSTALLIZED_SLIME_BLOCK.get())
        ;
    }
}
