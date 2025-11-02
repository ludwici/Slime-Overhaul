package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.CrumbSupplier;
import com.ludwici.slimeoverhaul.block.slimy.AncientSlimyBlock;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Function;

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

        ancientSlimyBlock(ANCIENT_AIR_SLIMY_BLOCK);
        ancientSlimyBlock(ANCIENT_WATER_SLIMY_BLOCK);
        ancientSlimyBlock(ANCIENT_EARTH_SLIMY_BLOCK);
        ancientSlimyBlock(ANCIENT_FIRE_SLIMY_BLOCK);

        createSlimeCoat(AIR_SLIME_COAT);
        createSlimeCoat(WATER_SLIME_COAT);
        createSlimeCoat(EARTH_SLIME_COAT);
        createSlimeCoat(FIRE_SLIME_COAT);
    }

    private void ancientSlimyBlock(CrumbSupplier<Block> block) {
        ancientSlimyBlock(block.get());
    }

    private void ancientSlimyBlock(Block block) {
        String id = name(block);
        Function<BlockState, ConfiguredModel[]> func = blockState -> ancientSlimyDustStates(blockState, (AncientSlimyBlock) block, id);
        getVariantBuilder(block).forAllStates(func);
    }

    protected ConfiguredModel[] ancientSlimyDustStates(BlockState state, AncientSlimyBlock block, String id) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(
                models().cubeAll(id + state.getValue(block.DUSTED), ResourceLocation.fromNamespaceAndPath(MODID, "block/ancient/" + id + "_stage" + state.getValue(block.DUSTED)))
        );
        simpleBlockItem(block, new ConfiguredModel(
                models().cubeAll(id + "0", ResourceLocation.fromNamespaceAndPath(MODID, "block/ancient/" + id + "_stage0"))
        ).model);
        return models;
    }

    private void slimeBlockWithItem(Block block) {
        ModelFile parent = new ConfiguredModel(models().withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), ResourceLocation.withDefaultNamespace("slime_block"))
                .texture("texture", blockTexture(block))
                .texture("particle", blockTexture(block))
                .renderType("translucent"))
                .model;
        simpleBlockWithItem(block, parent);
    }

    protected ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }

    public ModelFile createSlimeCoat(Block variant) {
        ModelFile model = new ConfiguredModel(
                models().withExistingParent(
                        name(variant),
                        ResourceLocation.fromNamespaceAndPath(MODID, "slime_coat_base")
                )
                        .texture("texture", ResourceLocation.fromNamespaceAndPath(MODID, "block/" + name(variant)))
                        .texture("particle", ResourceLocation.fromNamespaceAndPath(MODID, "block/" + name(variant)))
                        .renderType("translucent")
        ).model;

        getMultipartBuilder(variant)
                .part().modelFile(model).addModel().condition(BlockStateProperties.NORTH, true).end()
                .part().modelFile(model).rotationY(90).uvLock(true).addModel().condition(BlockStateProperties.EAST, true).end()
                .part().modelFile(model).rotationY(180).uvLock(true).addModel().condition(BlockStateProperties.SOUTH, true).end()
                .part().modelFile(model).rotationY(270).uvLock(true).addModel().condition(BlockStateProperties.WEST, true).end()
                .part().modelFile(model).rotationX(270).rotationY(0).uvLock(true).addModel().condition(BlockStateProperties.UP, true).end()
                .part().modelFile(model).rotationX(90).rotationY(0).uvLock(true).addModel().condition(BlockStateProperties.DOWN, true).end()
        ;

        return model;
    }

    public ModelFile createSlimeCoat(CrumbSupplier<Block> block) {
        return createSlimeCoat(block.get());
    }
}
