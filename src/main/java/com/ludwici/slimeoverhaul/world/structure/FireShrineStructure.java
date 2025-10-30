package com.ludwici.slimeoverhaul.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import java.util.*;

import static com.ludwici.slimeoverhaul.Content.*;

public class FireShrineStructure extends SinglePieceStructure {
    private static final FireShrinePiece.DeepslateBricksSelector DEEPSLATE_BRICKS_SELECTOR = new FireShrinePiece.DeepslateBricksSelector();
    private static final FireShrinePiece.DeepslateTilesSelector DEEPSLATE_TILES_SELECTOR = new FireShrinePiece.DeepslateTilesSelector();

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

            StructurePiece.BlockSelector selector = getSelector(randomSource);
            generateBox(worldGenLevel, boundingBox, 4, 0, 3, 4, 1, 3, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 7, 0, 3, 7, 1, 3, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 5, 2, 3, 6, 2, 3, false, randomSource, selector);

            selector = getSelector(randomSource);
            generateBox(worldGenLevel, boundingBox, 4, 0, 10, 4, 1, 10, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 7, 0, 10, 7, 1, 10, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 5, 2, 10, 6, 2, 10, false, randomSource, selector);

            selector = getSelector(randomSource);
            generateBox(worldGenLevel, boundingBox, 2, 0, 5, 2, 1, 5, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 2, 0, 8, 2, 1, 8, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 2, 2, 6, 2, 2, 7, false, randomSource, selector);

            selector = getSelector(randomSource);
            generateBox(worldGenLevel, boundingBox, 9, 0, 5, 9, 1, 5, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 9, 0, 8, 9, 1, 8, false, randomSource, selector);
            generateBox(worldGenLevel, boundingBox, 9, 2, 6, 9, 2, 7, false, randomSource, selector);

            int fireCount = 1 + randomSource.nextInt(4);

            List<BlockPos> shuffledPositions = new ArrayList<>(Arrays.asList(netherrackPositions));
            Collections.shuffle(shuffledPositions, new Random(randomSource.nextLong()));

            for (int i = 0; i < fireCount; i++) {
                BlockPos pos = shuffledPositions.get(i);
                placeBlock(worldGenLevel, Blocks.FIRE.defaultBlockState(), pos.getX(), pos.getY() + 1, pos.getZ(), boundingBox);
            }

            int count = randomSource.nextIntBetweenInclusive(0, 3);

            for (int i = 0; i < count; i++) {
                int randomX = randomSource.nextIntBetweenInclusive(3, 6);
                int randomZ = randomSource.nextIntBetweenInclusive(5, 9);
                int randomHeight = randomSource.nextIntBetweenInclusive(1, 3);

                for (int j = 0; j < randomHeight; j++) {
                    placeBlock(worldGenLevel, Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState(), randomX, j, randomZ, boundingBox);
                }

                placeBlock(worldGenLevel, ANCIENT_FIRE_SLIMY_BLOCK.get().defaultBlockState(), randomX, -1, randomZ, boundingBox);
            }

        }

        private StructurePiece.BlockSelector getSelector(RandomSource randomSource) {
            if (randomSource.nextFloat() < 0.4F) {
                return DEEPSLATE_BRICKS_SELECTOR;
            } else {
                return DEEPSLATE_TILES_SELECTOR;
            }
        }

        static class DeepslateBricksSelector extends StructurePiece.BlockSelector {
            @Override
            public void next(RandomSource randomSource, int i, int i1, int i2, boolean b) {
                if (randomSource.nextFloat() < 0.4F) {
                    this.next = Blocks.DEEPSLATE_BRICKS.defaultBlockState();
                } else {
                    this.next = Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
                }
            }
        }

        static class DeepslateTilesSelector extends StructurePiece.BlockSelector {
            @Override
            public void next(RandomSource randomSource, int i, int i1, int i2, boolean b) {
                if (randomSource.nextFloat() < 0.4F) {
                    this.next = Blocks.DEEPSLATE_TILES.defaultBlockState();
                } else {
                    this.next = Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState();
                }
            }
        }

    }
}
