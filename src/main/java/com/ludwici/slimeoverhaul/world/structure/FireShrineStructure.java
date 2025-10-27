package com.ludwici.slimeoverhaul.world.structure;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.VillagePools;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.fml.config.IConfigSpec;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class FireShrineStructure extends SinglePieceStructure {
    private static final ResourceLocation FLOOR = ResourceLocation.fromNamespaceAndPath(MODID, "fire_shrine_floor");

    private static final List<Block> DEEPSLATE = List.of(Blocks.DEEPSLATE_BRICKS, Blocks.CRACKED_DEEPSLATE_BRICKS);
    private static final List<Block> DEEPSLATE_TILES = List.of(Blocks.DEEPSLATE_TILES, Blocks.CRACKED_DEEPSLATE_TILES);
    private static final List<Block> BLACKSTONE = List.of(Blocks.POLISHED_BLACKSTONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);

//    private static final List<List<Block>> BLOCK_SELECTOR = List.of(DEEPSLATE, DEEPSLATE_TILES, BLACKSTONE);

    private static final FireShrinePiece.BlockSelector BLOCK_SELECTOR = new FireShrinePiece.BlockSelector();

    public static final MapCodec<FireShrineStructure> CODEC = simpleCodec(FireShrineStructure::new);

    protected FireShrineStructure(Structure.StructureSettings settings) {
        super(FireShrinePiece::new, 12, 12, settings);
    }

    @Override
    public StructureType<?> type() {
        return FIRE_SHRINE.get();
    }

    public static class FireShrinePiece extends ScatteredFeaturePiece {

        public FireShrinePiece(RandomSource random, int x, int z) {
            super(FIRE_SHRINE_PIECE.get(), x, 64, z, 12, 10, 12, getRandomHorizontalDirection(random));
        }

        public FireShrinePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            super(FIRE_SHRINE_PIECE.get(), compoundTag);
        }

        BlockPos[] netherrackPositions = {
                new BlockPos(5, -1, 3),
                new BlockPos(6, -1, 3),
                new BlockPos(5, -1, 10),
                new BlockPos(6, -1, 10),
                new BlockPos(2, -1, 6),
                new BlockPos(2, -1, 7),
                new BlockPos(9, -1, 6),
                new BlockPos(9, -1, 7)
        };

        @Override
        public void postProcess(WorldGenLevel worldGenLevel, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
            if (!updateAverageGroundHeight(worldGenLevel, boundingBox, 0)) {
                return;
            }

//            generateBox(worldGenLevel, boundingBox, 0, -1, 0, width, -1, width-1, false, randomSource, BLOCK_SELECTOR);
//            placeBlock(worldGenLevel, Blocks.LAPIS_BLOCK.defaultBlockState(), 0, -1, 0, boundingBox);

            for (BlockPos pos : netherrackPositions) {
                placeBlock(worldGenLevel, Blocks.NETHERRACK.defaultBlockState(), pos.getX(), pos.getY(), pos.getZ(), boundingBox);
            }

            generateBox(worldGenLevel, boundingBox, 4, 0, 3, 4, 1, 3, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 7, 0, 3, 7, 1, 3, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 5, 2, 3, 6, 2, 3, false, randomSource, BLOCK_SELECTOR);

            generateBox(worldGenLevel, boundingBox, 4, 0, 10, 4, 1, 10, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 7, 0, 10, 7, 1, 10, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 5, 2, 10, 6, 2, 10, false, randomSource, BLOCK_SELECTOR);

            generateBox(worldGenLevel, boundingBox, 2, 0, 5, 2, 1, 5, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 2, 0, 8, 2, 1, 8, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 2, 2, 6, 2, 2, 7, false, randomSource, BLOCK_SELECTOR);

            generateBox(worldGenLevel, boundingBox, 9, 0, 5, 9, 1, 5, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 9, 0, 8, 9, 1, 8, false, randomSource, BLOCK_SELECTOR);
            generateBox(worldGenLevel, boundingBox, 9, 2, 6, 9, 2, 7, false, randomSource, BLOCK_SELECTOR);

            int fireCount = 1 + randomSource.nextInt(4);

            List<BlockPos> shuffledPositions = new ArrayList<>(Arrays.asList(netherrackPositions));
            Collections.shuffle(shuffledPositions, new Random(randomSource.nextLong()));

            for (int i = 0; i < fireCount; i++) {
                BlockPos pos = shuffledPositions.get(i);
                placeBlock(worldGenLevel, Blocks.FIRE.defaultBlockState(), pos.getX(), pos.getY() + 1, pos.getZ(), boundingBox);
            }
        }

        static class BlockSelector extends StructurePiece.BlockSelector {

            @Override
            public void next(RandomSource randomSource, int i, int i1, int i2, boolean b) {
                if (randomSource.nextFloat() < 0.4F) {
                    this.next = Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
                } else {
                    this.next = Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
                }
            }
        }

    }
}
