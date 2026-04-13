package com.ludwici.slimeoverhaul.datagen;

import com.ludwici.crumbslib.api.BlockHelper;
import com.ludwici.crumbslib.api.CrumbSupplier;
import com.ludwici.crumbslib.api.ItemHelper;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.model.*;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.stream.Stream;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;
import static com.ludwici.slimeoverhaul.Content.*;


public class ModModelProvider extends ModelProvider {
    protected BlockModelGenerators blockModelGenerators;
    protected ItemModelGenerators itemModelGenerators;
    private static final ModelTemplate SLIME_BLOCK_TEMPLATE = new ModelTemplate(
            Optional.of(Identifier.withDefaultNamespace("block/slime_block")),
            Optional.empty(),
            TextureSlot.TEXTURE, TextureSlot.PARTICLE
    ).extend().build();

    public ModModelProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        blockModelGenerators = blockModels;
        itemModelGenerators = itemModels;

        slimeBlockWithItem(AIR_SLIME_BLOCK.get());
        slimeBlockWithItem(WATER_SLIME_BLOCK.get());
        slimeBlockWithItem(EARTH_SLIME_BLOCK.get());
        slimeBlockWithItem(FIRE_SLIME_BLOCK.get());

        flatItem(AIR_SLIME_BALL);
        flatItem(WATER_SLIME_BALL);
        flatItem(EARTH_SLIME_BALL);
        flatItem(FIRE_SLIME_BALL);

        flatItem(WATER_SLIME_BUCKET);
        flatItem(FIRE_SLIME_BUCKET);

        flatItem(ANCIENT_AIR_FRAGMENTS);
        flatItem(ANCIENT_WATER_FRAGMENTS);
        flatItem(ANCIENT_EARTH_FRAGMENTS);
        flatItem(ANCIENT_FIRE_FRAGMENTS);

        flatItem(AIR_CRYSTALLIZED_DUST);
        flatItem(WATER_CRYSTALLIZED_DUST);
        flatItem(EARTH_CRYSTALLIZED_DUST);
        flatItem(FIRE_CRYSTALLIZED_DUST);

        flatItem(AIR_SLIME_EGG);
        flatItem(WATER_SLIME_EGG);
        flatItem(EARTH_SLIME_EGG);
        flatItem(FLAME_SLIME_EGG);
//        flatItem(CLEANSING_BRUSH);

        blockModels.createBrushableBlock(ANCIENT_AIR_SLIMY_BLOCK.get());
        blockModels.createBrushableBlock(ANCIENT_WATER_SLIMY_BLOCK.get());
        blockModels.createBrushableBlock(ANCIENT_EARTH_SLIMY_BLOCK.get());
        blockModels.createBrushableBlock(ANCIENT_FIRE_SLIMY_BLOCK.get());

        createSlimeCoat(AIR_SLIME_COAT);
        createSlimeCoat(WATER_SLIME_COAT);
        createSlimeCoat(EARTH_SLIME_COAT);
        createSlimeCoat(FIRE_SLIME_COAT);

        crystallizedSlime(AIR_CRYSTALLIZED_SLIME_BLOCK);
        crystallizedSlime(WATER_CRYSTALLIZED_SLIME_BLOCK);
        crystallizedSlime(EARTH_CRYSTALLIZED_SLIME_BLOCK);
        crystallizedSlime(FIRE_CRYSTALLIZED_SLIME_BLOCK);

        blockModels.createBanner(SLIME_BANNER.get(), SLIME_BANNER_WALL.get(), DyeColor.WHITE);
    }

    private void flatItem(Item item) {
        blockModelGenerators.registerSimpleFlatItemModel(item);
    }

    public <I extends Item> void flatItem(CrumbSupplier<I> item) {
        flatItem(item.get());
    }

    private void crystallizedSlime(Block block) {
        blockModelGenerators.createTrivialCube(block);
//        getVariantBuilder(block).forAllStates(state -> {
//            Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
//            int stage = state.getValue(CrystallizedSlimeBlock.STAGE);
//            int yRot = getRotationForDirection(facing);
//            ModelFile model = new ConfiguredModel(
//                    models().withExistingParent(
//                                    name(block) + stage,
//                                    ResourceLocation.fromNamespaceAndPath(MODID, "crystallized_slime" + stage)
//                            )
//                            .texture("0", ResourceLocation.fromNamespaceAndPath(MODID, "block/" + name(block)))
//                            .texture("particle", ResourceLocation.fromNamespaceAndPath(MODID, "block/" + name(block)))
//            ).model;
//            ConfiguredModel.Builder<?> builder = ConfiguredModel.builder().modelFile(model);
//
//            builder.rotationY(yRot);
//
//            return builder.build();
//        });
    }

//    private int getRotationForDirection(Direction direction) {
//        return switch (direction) {
//            case EAST -> 90;
//            case SOUTH -> 180;
//            case WEST -> 270;
//            default -> 0;
//        };
//    }

    private void crystallizedSlime(CrumbSupplier<Block> block) {
        crystallizedSlime(block.get());
    }


    private void slimeBlockWithItem(Block block) {
        Identifier id = SLIME_BLOCK_TEMPLATE.create(
                block,
                new TextureMapping()
                        .put(TextureSlot.TEXTURE, TextureMapping.getBlockTexture(block))
                        .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(block)),
                blockModelGenerators.modelOutput
        );

        blockModelGenerators.blockStateOutput.accept(MultiVariantGenerator.dispatch(block, BlockModelGenerators.plainVariant(id)));
//        blockModelGenerators.createNonTemplateModelBlock(block);
//        blockModelGenerators.createTrivialCube(block);
        blockModelGenerators.createFlatItemModelWithBlockTexture(block.asItem(), block);

    }

    protected Identifier key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    protected String name(Block block) {
        return key(block).getPath();
    }

    public void createSlimeCoat(Block variant) {
        blockModelGenerators.createMultiface(variant);
    }

    public void createSlimeCoat(CrumbSupplier<Block> block) {
        createSlimeCoat(block.get());
    }

    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        return BlockHelper.BLOCKS.getEntries().stream();
    }

    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return ItemHelper.ITEMS.getEntries().stream().filter(i -> i.get() != CLEANSING_BRUSH.get());
    }
}
