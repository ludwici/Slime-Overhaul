package com.ludwici.slimeoverhaul;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class GlowingSlimeBlock extends SlimeBlock {
    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    public GlowingSlimeBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any()
                .setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIT);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult pHit) {
        ItemStack stack = player.getItemInHand(interactionHand);
        if (stack.is(Items.GLOW_INK_SAC)) {
            if (!blockState.getValue(LIT)) {
                blockState = blockState.setValue(LIT, true);
                level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
                level.updateNeighborsAt(blockPos, this);
                level.levelEvent(player, 3003, blockPos, 0);
                stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        } else if (stack.is(ItemTags.AXES)) {
            if (blockState.getValue(LIT)) {
                blockState = blockState.setValue(LIT, false);
                level.setBlock(blockPos, blockState, Block.UPDATE_ALL);
                level.updateNeighborsAt(blockPos, this);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(blockState, level, blockPos, player, interactionHand, pHit);
    }
}
