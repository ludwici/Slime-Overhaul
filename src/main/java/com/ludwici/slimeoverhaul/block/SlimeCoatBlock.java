package com.ludwici.slimeoverhaul.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SlimeCoatBlock extends Block {
    public static final IntegerProperty FACE_COUNT = IntegerProperty.create("face_count", 0, 6);
    public static final BooleanProperty UP = PipeBlock.UP;
    public static final BooleanProperty DOWN = PipeBlock.DOWN;
    public static final BooleanProperty NORTH = PipeBlock.NORTH;
    public static final BooleanProperty EAST = PipeBlock.EAST;
    public static final BooleanProperty SOUTH = PipeBlock.SOUTH;
    public static final BooleanProperty WEST = PipeBlock.WEST;

    public static final Map<Direction, BooleanProperty> PROPERTY_BY_DIRECTION = PipeBlock.PROPERTY_BY_DIRECTION
            .entrySet()
            .stream()
            .collect(Util.toMap());

    private static final VoxelShape UP_AABB = Block.box(0.0, 15.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape DOWN_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 1.0, 16.0);
    private static final VoxelShape WEST_AABB = Block.box(0.0, 0.0, 0.0, 1.0, 16.0, 16.0);
    private static final VoxelShape EAST_AABB = Block.box(15.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    private static final VoxelShape NORTH_AABB = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 1.0);
    private static final VoxelShape SOUTH_AABB = Block.box(0.0, 0.0, 15.0, 16.0, 16.0, 16.0);
    private final Map<BlockState, VoxelShape> shapesCache;

    public SlimeCoatBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(UP, Boolean.FALSE)
                        .setValue(DOWN, Boolean.FALSE)
                        .setValue(NORTH, Boolean.FALSE)
                        .setValue(EAST, Boolean.FALSE)
                        .setValue(SOUTH, Boolean.FALSE)
                        .setValue(WEST, Boolean.FALSE)
                        .setValue(FACE_COUNT, 0)
        );

        this.shapesCache = ImmutableMap.copyOf(
                this.stateDefinition.getPossibleStates().stream().collect(Collectors.toMap(Function.identity(), SlimeCoatBlock::calculateShape))
        );
    }

    protected static VoxelShape calculateShape(BlockState pState) {
        VoxelShape voxelshape = Shapes.empty();

        if (pState.getValue(UP)) {
            voxelshape = UP_AABB;
        }

        if (pState.getValue(DOWN)) {
            voxelshape = Shapes.or(voxelshape, DOWN_AABB);
        }

        if (pState.getValue(NORTH)) {
            voxelshape = Shapes.or(voxelshape, NORTH_AABB);
        }

        if (pState.getValue(SOUTH)) {
            voxelshape = Shapes.or(voxelshape, SOUTH_AABB);
        }

        if (pState.getValue(EAST)) {
            voxelshape = Shapes.or(voxelshape, EAST_AABB);
        }

        if (pState.getValue(WEST)) {
            voxelshape = Shapes.or(voxelshape, WEST_AABB);
        }

        return voxelshape.isEmpty() ? Shapes.block() : voxelshape;
    }

    @Override
    protected VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return this.shapesCache.get(pState);
    }

    public static boolean isAcceptableNeighbour(BlockGetter pBlockReader, BlockPos pNeighborPos, Direction pAttachedFace) {
        return MultifaceBlock.canAttachTo(pBlockReader, pAttachedFace, pNeighborPos, pBlockReader.getBlockState(pNeighborPos));
    }

    public static BooleanProperty getPropertyForFace(Direction pFace) {
        return PROPERTY_BY_DIRECTION.get(pFace);
    }

    protected boolean canSupportAtFace(BlockGetter pLevel, BlockPos pPos, Direction pDirection) {
        BlockPos blockpos = pPos.relative(pDirection);
        if (isAcceptableNeighbour(pLevel, blockpos, pDirection)) {
            return true;
        } else {
            BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(pDirection);
            BlockState blockstate = pLevel.getBlockState(pPos.above());
            return blockstate.is(this) && blockstate.getValue(booleanproperty);
        }
    }

    @Override
    protected boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        BlockState blockstate = pUseContext.getLevel().getBlockState(pUseContext.getClickedPos());

        boolean hasParts = false;
        ItemStack itemStack = pUseContext.getItemInHand();
        boolean same = itemStack.is(this.asItem());

        int faces = this.countFaces(blockstate);

        if (pUseContext.getPlayer() != null) {
            for (Direction direction : pUseContext.getNearestLookingDirections()) {
                BooleanProperty booleanproperty = getPropertyForFace(direction);
                boolean flag1 = booleanproperty != null ? blockstate.getValue(booleanproperty) : false;
                if (!flag1 && same && this.canSupportAtFace(pUseContext.getLevel(), pUseContext.getClickedPos(), direction)) {
                    hasParts = true;
                    break;
                }
            }

            if (same && pUseContext.replacingClickedOnBlock()) {
                if (pUseContext.getClickedFace() == Direction.UP && !hasParts) {
                    return false;
                }
            }
        } else {
            return false;
        }

        return (blockstate.is(this) && same) && faces < PROPERTY_BY_DIRECTION.size();
    }

    protected BlockState getUpdatedState(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        BlockPos blockpos = pPos.above();

        BlockState blockstate = null;

        int count = 0;

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            BooleanProperty booleanproperty = getPropertyForFace(direction);
            if (pState.getValue(booleanproperty)) {
                boolean flag = this.canSupportAtFace(pLevel, pPos, direction);
                if (!flag) {
                    if (blockstate == null) {
                        blockstate = pLevel.getBlockState(blockpos);
                    }

                    flag = blockstate.is(this) && blockstate.getValue(booleanproperty);
                }

                pState = pState.setValue(booleanproperty, flag);
                if (flag) {
                    count++;
                }
            }
        }

        count = count + (pState.getValue(DOWN) ? 1 : 0) + (pState.getValue(UP) ? 1 : 0);
        pState = pState.setValue(FACE_COUNT, count);
        return pState;
    }

    protected boolean hasFaces(BlockState pState) {
        return this.countFaces(pState) > 0;
    }

    public int countFaces(BlockState pState) {
        int i = 0;

        for (BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values()) {
            if (pState.getValue(booleanproperty)) {
                i++;
            }
        }
        return i;
    }

    @Override
    protected boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return super.canSurvive(pState, pLevel, pPos) && this.hasFaces(this.getUpdatedState(pState, pLevel, pPos)) || (pState.getValue(DOWN));
    }

    @Override
    protected BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        int faces = this.countFaces(pState);
        if (faces == 1) {
            if ((pState.getValue(UP) && pLevel.isEmptyBlock(pCurrentPos.above())) || (pState.getValue(DOWN) && pLevel.isEmptyBlock(pCurrentPos.below()))) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        BlockState blockstate = this.getUpdatedState(pState, pLevel, pCurrentPos);
        return !this.hasFaces(blockstate) ? Blocks.AIR.defaultBlockState() : blockstate;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockPos blockpos = pContext.getClickedPos();
        BlockState blockstate = pContext.getLevel().getBlockState(blockpos);
        BlockState blockstate1;
        boolean flag = false;
        if (blockstate.is(this)) {
            blockstate1 = blockstate;
            flag = true;
        } else if (blockstate.getBlock() instanceof CarpetBlock) {
            blockstate1 = this.defaultBlockState().setValue(DOWN, true).setValue(FACE_COUNT, 1);
        }
        else {
            blockstate1 = this.defaultBlockState();
        }

        for (Direction direction : pContext.getNearestLookingDirections()) {
            BooleanProperty booleanproperty = getPropertyForFace(direction);
            boolean flag1 = flag && blockstate.getValue(booleanproperty);
            if (!flag1 && this.canSupportAtFace(pContext.getLevel(), pContext.getClickedPos(), direction)) {
                return blockstate1.setValue(booleanproperty, Boolean.TRUE).setValue(FACE_COUNT, blockstate1.getValue(FACE_COUNT) + 1);
            }
        }

        if (!flag) {
            return blockstate1.setValue(DOWN, true).setValue(FACE_COUNT, 1);
        }

        return blockstate1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(UP, DOWN, NORTH, EAST, SOUTH, WEST, FACE_COUNT);
    }

    @Override
    protected BlockState rotate(BlockState pState, Rotation pRotate) {
        return switch (pRotate) {
            case CLOCKWISE_180 -> pState.setValue(NORTH, pState.getValue(SOUTH))
                    .setValue(EAST, pState.getValue(WEST))
                    .setValue(SOUTH, pState.getValue(NORTH))
                    .setValue(WEST, pState.getValue(EAST));
            case COUNTERCLOCKWISE_90 -> pState.setValue(NORTH, pState.getValue(EAST))
                    .setValue(EAST, pState.getValue(SOUTH))
                    .setValue(SOUTH, pState.getValue(WEST))
                    .setValue(WEST, pState.getValue(NORTH));
            case CLOCKWISE_90 -> pState.setValue(NORTH, pState.getValue(WEST))
                    .setValue(EAST, pState.getValue(NORTH))
                    .setValue(SOUTH, pState.getValue(EAST))
                    .setValue(WEST, pState.getValue(SOUTH));
            default -> pState;
        };
    }

    @Override
    protected BlockState mirror(BlockState pState, Mirror pMirror) {
        return switch (pMirror) {
            case LEFT_RIGHT -> pState.setValue(NORTH, pState.getValue(SOUTH)).setValue(SOUTH, pState.getValue(NORTH));
            case FRONT_BACK -> pState.setValue(EAST, pState.getValue(WEST)).setValue(WEST, pState.getValue(EAST));
            default -> super.mirror(pState, pMirror);
        };
    }
}
