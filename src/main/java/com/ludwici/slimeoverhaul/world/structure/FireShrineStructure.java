package com.ludwici.slimeoverhaul.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.ScatteredFeaturePiece;
import net.minecraft.world.level.levelgen.structure.SinglePieceStructure;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureManager;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

import static com.ludwici.slimeoverhaul.Content.ANCIENT_FIRE_SLIMY_BLOCK;
import static com.ludwici.slimeoverhaul.Content.FIRE_SHRINE;
import static com.ludwici.slimeoverhaul.Content.FIRE_SHRINE_PIECE;

public class FireShrineStructure extends SinglePieceStructure {
    public static final MapCodec<FireShrineStructure> CODEC = simpleCodec(FireShrineStructure::new);

    protected FireShrineStructure(Structure.StructureSettings settings) {
        super(FireShrinePiece::new, 12, 12, settings);
    }

    @Override
    public StructureType<?> type() {
        return FIRE_SHRINE.get();
    }

    public static class FireShrinePiece extends ScatteredFeaturePiece {
        private static final int SIZE = 12;
        private static final int MIN = 1;
        private static final int MAX = 10;

        public FireShrinePiece(RandomSource random, int x, int z) {
            super(FIRE_SHRINE_PIECE.get(), x, 64, z, SIZE, 7, SIZE, getRandomHorizontalDirection(random));
        }

        public FireShrinePiece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(FIRE_SHRINE_PIECE.get(), tag);
        }

        @Override
        public void postProcess(WorldGenLevel level, StructureManager structureManager, ChunkGenerator chunkGenerator, RandomSource randomSource, BoundingBox chunkBox, ChunkPos chunkPos, BlockPos blockPos) {
            if (!updateAverageGroundHeight(level, chunkBox, 0)) {
                return;
            }

            RandomSource random = RandomSource.create(Mth.getSeed(this.boundingBox.minX(), 0, this.boundingBox.minZ()));
            int palette = random.nextInt(3);

            for (int x = MIN; x <= MAX; x++) {
                for (int z = MIN; z <= MAX; z++) {
                    boolean altar = x >= 4 && x <= 7 && z >= 4 && z <= 7;
                    boolean pillar = isPillar(x, z);

                    if (!altar && !pillar && random.nextFloat() < 0.18F) {
                        continue;
                    }

                    BlockState floor = randomStone(random, palette);
                    placeBlock(level, floor, x, 0, z, chunkBox);
                    fillColumnDown(level, Blocks.COBBLED_DEEPSLATE.defaultBlockState(), x, -1, z, chunkBox);
                }
            }

            for (int x = 2; x <= 9; x++) {
                for (int z = 2; z <= 9; z++) {
                    if (isPillar(x, z)) {
                        buildPillar(level, chunkBox, random, palette, x, z);
                        continue;
                    }

                    boolean outerWall = x == 2 || x == 9 || z == 2 || z == 9;
                    if (outerWall && random.nextFloat() < 0.36F) {
                        int height = 1 + random.nextInt(2);
                        for (int y = 1; y <= height; y++) {
                            if (random.nextFloat() < 0.16F) {
                                break;
                            }

                            placeBlock(level, randomStone(random, palette), x, y, z, chunkBox);
                        }
                    }
                }
            }

            for (int x = 4; x <= 7; x++) {
                for (int z = 4; z <= 7; z++) {
                    if (random.nextFloat() < 0.13F) {
                        continue;
                    }

                    placeBlock(level, randomStone(random, palette), x, 1, z, chunkBox);
                }
            }

            placeBlock(level, Blocks.NETHERRACK.defaultBlockState(), 5, 1, 5, chunkBox);
            placeBlock(level, Blocks.FIRE.defaultBlockState(), 5, 2, 5, chunkBox);

            int[][] braziers = {{3, 5}, {8, 5}, {5, 3}, {5, 8}};
            for (int[] pos : braziers) {
                if (random.nextFloat() < 0.5F) {
                    placeBlock(level, Blocks.NETHERRACK.defaultBlockState(), pos[0], 1, pos[1], chunkBox);
                    placeBlock(level, Blocks.FIRE.defaultBlockState(), pos[0], 2, pos[1], chunkBox);
                }
            }

            int[] ancientPositions = {4, 7};
            for (int x : ancientPositions) {
                for (int z : ancientPositions) {
                    if (random.nextFloat() < 0.55F) {
                        placeBlock(level, ANCIENT_FIRE_SLIMY_BLOCK.get().defaultBlockState(), x, 1, z, chunkBox);
                    }
                }
            }

            for (int x = 0; x < SIZE; x++) {
                for (int z = 0; z < SIZE; z++) {
                    if (x >= MIN && x <= MAX && z >= MIN && z <= MAX) {
                        continue;
                    }

                    if (random.nextFloat() < 0.15F) {
                        placeBlock(level, randomStone(random, palette), x, 0, z, chunkBox);
                        fillColumnDown(level, Blocks.COBBLED_DEEPSLATE.defaultBlockState(), x, -1, z, chunkBox);
                    }
                }
            }
        }

        private void buildPillar(WorldGenLevel level, BoundingBox chunkBox, RandomSource random, int palette, int x, int z) {
            int height = 2 + random.nextInt(4);
            for (int y = 1; y <= height; y++) {
                if (y >= 3 && random.nextFloat() < 0.2F) {
                    break;
                }

                placeBlock(level, randomStone(random, palette), x, y, z, chunkBox);
            }
        }

        private boolean isPillar(int x, int z) {
            return (x == 2 || x == 9) && (z == 2 || z == 9);
        }

        private BlockState randomStone(RandomSource random, int palette) {
            float value = random.nextFloat();
            if (palette == 0) {
                if (value < 0.45F) {
                    return Blocks.DEEPSLATE_BRICKS.defaultBlockState();
                }
                if (value < 0.75F) {
                    return Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
                }
                if (value < 0.90F) {
                    return Blocks.DEEPSLATE_TILES.defaultBlockState();
                }
                return Blocks.CHISELED_DEEPSLATE.defaultBlockState();
            }

            if (palette == 1) {
                if (value < 0.40F) {
                    return Blocks.DEEPSLATE_TILES.defaultBlockState();
                }
                if (value < 0.72F) {
                    return Blocks.CRACKED_DEEPSLATE_TILES.defaultBlockState();
                }
                if (value < 0.90F) {
                    return Blocks.POLISHED_DEEPSLATE.defaultBlockState();
                }
                return Blocks.DEEPSLATE_BRICKS.defaultBlockState();
            }

            if (value < 0.38F) {
                return Blocks.POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
            }
            if (value < 0.68F) {
                return Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS.defaultBlockState();
            }
            if (value < 0.88F) {
                return Blocks.BLACKSTONE.defaultBlockState();
            }
            return Blocks.GILDED_BLACKSTONE.defaultBlockState();
        }
    }
}
