package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.*;
import static com.ludwici.slimeoverhaul.Content.*;


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
        tag(SLIMES).add(THUNDER_SLIME.get());

        tag(ELEMENTAL_SLIMES).add(AIR_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(WATER_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(EARTH_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(FLAME_SLIME.get());
        tag(ELEMENTAL_SLIMES).add(THUNDER_SLIME.get());

        tag(EntityTypeTags.AQUATIC).add(WATER_SLIME.get()).replace(false);
        tag(EntityTypeTags.CAN_BREATHE_UNDER_WATER).add(WATER_SLIME.get()).replace(false);

        tag(EntityTypeTags.IMMUNE_TO_OOZING).addTag(SLIMES).replace(false);
    }

    private <T extends Entity> void immuneToOozing(Supplier<EntityType<T>> entity) {
        tag(EntityTypeTags.IMMUNE_TO_OOZING).add(entity.get()).replace(false);
    }
}
