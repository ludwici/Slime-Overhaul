package com.ludwici.slimeoverhaul.block.crystallized;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PyrocideBlock extends CrystallizedSlimeBlock {
    private static final VoxelShape STAGE_1_NORTH = Block.box(3, 0, 4, 12, 13, 12);
    private static final VoxelShape STAGE_2_NORTH = Block.box(3, 0, 4, 12, 13, 12);
    private static final VoxelShape STAGE_3_NORTH = Block.box(3, 0, 4, 12, 13, 12);

//    private static final VoxelShape[] STAGE_1_SHAPES = createRotatedShapes(STAGE_1_NORTH);
//    private static final VoxelShape[] STAGE_2_SHAPES = createRotatedShapes(STAGE_2_NORTH);
//    private static final VoxelShape[] STAGE_3_SHAPES = createRotatedShapes(STAGE_3_NORTH);


    public PyrocideBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return STAGE_1_NORTH;
//        int stage = state.getValue(STAGE);
//        Direction facing = state.getValue(FACING);
//
//        VoxelShape[] shapes = switch (stage) {
//            case 2 -> STAGE_2_SHAPES;
//            case 3 -> STAGE_3_SHAPES;
//            default -> STAGE_1_SHAPES;
//        };
//
//        return shapes[facing.get2DDataValue()];
    }

    /**
     * Создает массив повернутых форм для всех горизонтальных направлений
     * Индекс массива соответствует Direction.get2DDataValue():
     * 0 = SOUTH, 1 = WEST, 2 = NORTH, 3 = EAST
     */
    private static VoxelShape[] createRotatedShapes(VoxelShape baseShape) {
        VoxelShape[] shapes = new VoxelShape[4];

        // NORTH (базовая форма)
        shapes[Direction.NORTH.get2DDataValue()] = baseShape;

        // EAST (поворот на 90° по часовой стрелке)
        shapes[Direction.EAST.get2DDataValue()] = rotateShape(baseShape, Direction.NORTH, Direction.EAST);

        // SOUTH (поворот на 180°)
        shapes[Direction.SOUTH.get2DDataValue()] = rotateShape(baseShape, Direction.NORTH, Direction.SOUTH);

        // WEST (поворот на 270° по часовой стрелке)
        shapes[Direction.WEST.get2DDataValue()] = rotateShape(baseShape, Direction.NORTH, Direction.WEST);

        return shapes;
    }

    /**
     * Поворачивает VoxelShape из одного направления в другое
     */
    private static VoxelShape rotateShape(VoxelShape shape, Direction from, Direction to) {
        if (from == to) {
            return shape;
        }

        VoxelShape[] buffer = {shape};
        int rotations = (to.get2DDataValue() - from.get2DDataValue() + 4) % 4;

        for (int i = 0; i < rotations; i++) {
            buffer[0] = buffer[0].move(0.5, 0, 0.5);
            buffer[0] = rotateShapeBy90(buffer[0]);
            buffer[0] = buffer[0].move(-0.5, 0, -0.5);
        }

        return buffer[0];
    }

    /**
     * Поворачивает форму на 90° по часовой стрелке вокруг оси Y
     */
    private static VoxelShape rotateShapeBy90(VoxelShape shape) {
        double[] minBounds = {16, 16, 16};
        double[] maxBounds = {0, 0, 0};

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            // Поворот координат: новый X = 16 - старый Z, новый Z = старый X
            double newMinX = (1 - maxZ) * 16;
            double newMaxX = (1 - minZ) * 16;
            double newMinZ = minX * 16;
            double newMaxZ = maxX * 16;

            minBounds[0] = Math.min(minBounds[0], newMinX);
            minBounds[1] = Math.min(minBounds[1], minY * 16);
            minBounds[2] = Math.min(minBounds[2], newMinZ);

            maxBounds[0] = Math.max(maxBounds[0], newMaxX);
            maxBounds[1] = Math.max(maxBounds[1], maxY * 16);
            maxBounds[2] = Math.max(maxBounds[2], newMaxZ);
        });

        return Block.box(minBounds[0], minBounds[1], minBounds[2],
                maxBounds[0], maxBounds[1], maxBounds[2]);
    }
}
