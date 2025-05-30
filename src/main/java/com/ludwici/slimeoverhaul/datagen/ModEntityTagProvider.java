package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;


public class ModEntityTagProvider extends EntityTypeTagsProvider {
    public ModEntityTagProvider(PackOutput arg, CompletableFuture<HolderLookup.Provider> completableFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(arg, completableFuture, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider arg) {
        tag(SLIMES).add(EntityType.SLIME);
        tag(SLIMES).add(AIR_SLIME.get());
        tag(SLIMES).add(WATER_SLIME.get());
        tag(SLIMES).add(EARTH_SLIME.get());
        tag(SLIMES).add(FLAME_SLIME.get());

        tag(ELEMENTAL_SLIMES).add(AIR_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(WATER_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(EARTH_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(FLAME_SLIME.get());
    }
}
