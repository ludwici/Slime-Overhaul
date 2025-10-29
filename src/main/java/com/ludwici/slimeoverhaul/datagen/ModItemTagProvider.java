package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.*;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, CompletableFuture<TagLookup<Block>> completableFuture2, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, completableFuture2, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.SLIME_BALLS)
                .add(AIR_SLIME_BALL.get())
                .add(WATER_SLIME_BALL.get())
                .add(EARTH_SLIME_BALL.get())
                .add(FIRE_SLIME_BALL.get())
        ;

        tag(Tags.Items.TOOLS).add(CLEANSING_BRUSH.get());
        tag(Tags.Items.TOOLS_BRUSH).add(CLEANSING_BRUSH.get());

        tag(ANCIENT_FIRE_FRAGMENTS_TAG)
                .add(ANCIENT_FIRE_FRAGMENTS.get());
    }
}
