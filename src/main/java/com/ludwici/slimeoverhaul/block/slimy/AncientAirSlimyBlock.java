package com.ludwici.slimeoverhaul.block.slimy;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

public class AncientAirSlimyBlock extends AncientSlimyBlock{
    public AncientAirSlimyBlock(Properties properties) {
        super(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
    }
}
