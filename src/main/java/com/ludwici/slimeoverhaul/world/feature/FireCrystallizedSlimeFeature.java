package com.ludwici.slimeoverhaul.world.feature;

import com.ludwici.slimeoverhaul.block.crystallized.CrystallizedSlimeBlock;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import static com.ludwici.slimeoverhaul.Content.FIRE_CRYSTALLIZED_SLIME_BLOCK;

public class FireCrystallizedSlimeFeature extends Feature<NoneFeatureConfiguration> {
    public FireCrystallizedSlimeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        RandomSource random = context.random();
        BlockPos origin = context.origin();
        WorldGenLevel level = context.level();

        boolean placed = false;

        for (int i = 0; i < 64; i++) {
            BlockPos pos = origin.offset(random.nextInt(16), random.nextInt(64), random.nextInt(16));

            boolean isSolidBelow = level.getBlockState(pos.below()).isSolid();

            BlockPos below = pos.below();
            BlockPos[] surroundingPositions = {
                    below.north(),
                    below.south(),
                    below.east(),
                    below.west()
            };
            boolean hasLava = false;
            for (BlockPos checkPos : surroundingPositions) {
                BlockState state1 = level.getBlockState(checkPos);
                if (state1.is(Blocks.LAVA)) {
                    hasLava = true;
                    break;
                }
            }

            if (isSolidBelow && hasLava) {
                surroundingPositions = new BlockPos[]{
                        pos.north(),
                        pos.south(),
                        pos.east(),
                        pos.west()
                };
                boolean allSolid = true;
                for (BlockPos checkPos : surroundingPositions) {
                    BlockState state1 = level.getBlockState(checkPos);
                    if (!state1.isSolid() && !state1.is(Blocks.LAVA)) {
                        allSolid = false;
                        break;
                    }
                }

                if (!allSolid) {
                    int stage = random.nextIntBetweenInclusive(CrystallizedSlimeBlock.MIN_STAGE, CrystallizedSlimeBlock.MAX_STAGE);
                    Direction randDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                    level.setBlock(pos, FIRE_CRYSTALLIZED_SLIME_BLOCK.get().defaultBlockState().setValue(CrystallizedSlimeBlock.STAGE, stage).setValue(CrystallizedSlimeBlock.FACING, randDir), 2);
                    placed = true;
                }
            }
        }

        return placed;
    }
}
