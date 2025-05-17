package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModBlockModelProvider extends BlockStateProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        slimeBlockWithItem(AIR_SLIME_BLOCK.get());
        slimeBlockWithItem(WATER_SLIME_BLOCK.get());
        slimeBlockWithItem(EARTH_SLIME_BLOCK.get());
        slimeBlockWithItem(FIRE_SLIME_BLOCK.get());
        slimeBlockWithItem(THUNDER_SLIME_BLOCK.get());
    }

    private void slimeBlockWithItem(Block block) {
        ModelFile parent = new ConfiguredModel(models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), ResourceLocation.withDefaultNamespace("slime_block"))
                .texture("texture", blockTexture(block))
                .texture("particle", blockTexture(block))
                .renderType("translucent"))
                .model;
        simpleBlockWithItem(block, parent);
    }
}
