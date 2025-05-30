package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.BlockHelper;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

import static com.ludwici.slimeoverhaul.Content.*;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    protected ModBlockLootTableProvider() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {
        dropSelf(AIR_SLIME_BLOCK.get());
        dropSelf(WATER_SLIME_BLOCK.get());
        dropSelf(EARTH_SLIME_BLOCK.get());
        dropSelf(FLAME_SLIME_BLOCK.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlockHelper.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
