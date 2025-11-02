package com.ludwici.slimeoverhaul.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrystallizedSlimeBlock extends Block {
    public static final MapCodec<CrystallizedSlimeBlock> CODEC = simpleCodec(CrystallizedSlimeBlock::new);

    public static final int MIN_STAGE = 1;
    public static final int MAX_STAGE = 3;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty STAGE = IntegerProperty.create("stage", MIN_STAGE, MAX_STAGE);

    private static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);


    public CrystallizedSlimeBlock(Properties p_52591_) {
        super(p_52591_);
        registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(STAGE, MIN_STAGE)
        );
    }

    @Override
    protected MapCodec<CrystallizedSlimeBlock> codec() {
        return CODEC;
    }

//    @Override
//    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
//        return DOWN_AABB;
//    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = context.getLevel().getBlockState(context.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(STAGE, Math.min(MAX_STAGE, blockState.getValue(STAGE) + 1));
        }

        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState below = level.getBlockState(pos.below());
        return !below.isAir() && !(below.getBlock() instanceof CrystallizedSlimeBlock);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, STAGE);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return  state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext useContext) {
        if (!useContext.isSecondaryUseActive() && useContext.getItemInHand().is(this.asItem()) && state.getValue(STAGE) < MAX_STAGE) {
            return true;
        }
        return super.canBeReplaced(state, useContext);
    }
}
