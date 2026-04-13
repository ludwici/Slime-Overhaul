package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.VillagerTradesTagsProvider;
import net.minecraft.tags.VillagerTradeTags;

import java.util.concurrent.CompletableFuture;

import static com.ludwici.slimeoverhaul.world.item.ModTrades.*;

public class ModVillageTradesTagProvider extends VillagerTradesTagsProvider {

    public ModVillageTradesTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void addTags(HolderLookup.Provider registries) {
        tag(VillagerTradeTags.CARTOGRAPHER_LEVEL_2)
                .add(CARTOGRAPHER_2_EMERALD_PATTERN_SLIME)
        ;
    }
}
