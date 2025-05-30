package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModBannerTagProvider extends TagsProvider<BannerPattern> {
    protected ModBannerTagProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, Registries.BANNER_PATTERN, pLookupProvider, MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        tag(SLIME_BANNER_PATTERN).add(PATTERN_SLIME_REG.getKey());
        tag(AIR_SLIME_SIGN_BANNER_PATTERN).add(PATTERN_AIR_SLIME_SIGN_REG.getKey());
        tag(WATER_SLIME_SIGN_BANNER_PATTERN).add(PATTERN_WATER_SLIME_SIGN_REG.getKey());
        tag(EARTH_SLIME_SIGN_BANNER_PATTERN).add(PATTERN_EARTH_SLIME_SIGN_REG.getKey());
        tag(FIRE_SLIME_SIGN_BANNER_PATTERN).add(PATTERN_FIRE_SLIME_SIGN_REG.getKey());
    }
}
