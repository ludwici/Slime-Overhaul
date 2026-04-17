package com.ludwici.slimeoverhaul.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import static com.ludwici.slimeoverhaul.Content.*;

public class FireThickenerCauldronBlock extends AbstractCauldronBlock {
    public static final MapCodec<FireThickenerCauldronBlock> CODEC = simpleCodec(FireThickenerCauldronBlock::new);
    public static CauldronInteraction.InteractionMap THICKENER = CauldronInteraction.newInteractionMap("thickener");
    public static CauldronInteraction FILL_PYDOCIDE_DUST = ((state, level, pos, player, hand, stack) -> {
        if (!level.isClientSide()) {
            if (stack.is(PYROCIDE_DUST.get())) {
                if (stack.getCount() >= 10) {
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    stack.shrink(10);
                    level.setBlockAndUpdate(pos, Blocks.CAULDRON.defaultBlockState());
                    Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(FIRE_SLIME_BALL.get()));
                }
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    });

    public static CauldronInteraction FILL_FIRE_SLIME = ((state, level, pos, player, hand, stack) -> {
        if (!level.isClientSide()) {
            if (stack.is(FIRE_SLIME_BALL.get())) {
                if (stack.getCount() >= 10) {
                    player.awardStat(Stats.ITEM_USED.get(stack.getItem()));
                    player.awardStat(Stats.FILL_CAULDRON);
                    stack.shrink(10);
                    level.setBlockAndUpdate(pos, FIRE_THICKENER_CAULDRON_BLOCK.get().defaultBlockState());
                }
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    });

    public FireThickenerCauldronBlock(Properties properties) {
        super(properties, THICKENER);
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean isFull(BlockState state) {
        return true;
    }

    @Override
    protected double getContentHeight(BlockState state) {
        return 0.9375;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos) {
        return 3;
    }


}
