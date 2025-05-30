package com.ludwici.slimeoverhaul.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class ModBlockModelProvider extends BlockStateProvider {
    public ModBlockModelProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        slimeBlockWithItem(AIR_SLIME_BLOCK.get());
        slimeBlockWithItem(WATER_SLIME_BLOCK.get());
        slimeBlockWithItem(EARTH_SLIME_BLOCK.get());
        slimeBlockWithItem(FLAME_SLIME_BLOCK.get());
    }

    private void slimeBlockWithItem(Block block) {
        ModelFile parent = new ConfiguredModel(models().withExistingParent(ForgeRegistries.BLOCKS.getKey(block).getPath(), ResourceLocation.withDefaultNamespace("slime_block"))
                .texture("texture", blockTexture(block))
                .texture("particle", blockTexture(block))
                .renderType("translucent"))
                .model;
        simpleBlockWithItem(block, parent);
    }
}
