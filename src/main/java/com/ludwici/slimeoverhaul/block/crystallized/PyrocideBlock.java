package com.ludwici.slimeoverhaul.block.crystallized;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PyrocideBlock extends CrystallizedSlimeBlock {
    private static final VoxelShape STAGE_UP = Block.box(3, 0, 3, 11, 13, 12);
    private static final VoxelShape STAGE_DOWN = Block.box(3, 3, 4, 11, 16, 13);
    private static final VoxelShape STAGE_NORTH = Block.box(3, 3, 3, 11, 12, 16);
    private static final VoxelShape STAGE_SOUTH = Block.box(5, 3, 0, 13, 12, 13);
    private static final VoxelShape STAGE_EAST = Block.box(0, 3, 3, 13, 12, 11);
    private static final VoxelShape STAGE_WEST = Block.box(3, 3, 5, 16, 12, 13);

    public PyrocideBlock(Properties properties) {
        super(properties);
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        return switch (facing) {
            case DOWN -> STAGE_DOWN;
            case UP -> STAGE_UP;
            case NORTH -> STAGE_NORTH;
            case SOUTH -> STAGE_SOUTH;
            case WEST -> STAGE_WEST;
            case EAST -> STAGE_EAST;
        };
    }
}
