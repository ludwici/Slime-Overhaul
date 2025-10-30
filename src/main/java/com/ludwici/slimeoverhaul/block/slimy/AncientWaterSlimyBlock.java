package com.ludwici.slimeoverhaul.block.slimy;

import com.ludwici.slimeoverhaul.block.entities.AncientSlimyBlockEntity;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;

public class AncientWaterSlimyBlock extends AncientSlimyBlock{
    public AncientWaterSlimyBlock(Properties properties) {
        super(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
    }

    @Override
    public void applyEffect(Player player, AncientSlimyBlockEntity blockEntity) {
//        blockEntity.getLevel().setBlock(player.getOnPos().above(), Blocks.WATER.defaultBlockState(), 3);
    }
}
