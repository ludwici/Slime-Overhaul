package com.ludwici.slimeoverhaul.block.slimy;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

import static com.ludwici.slimeoverhaul.Content.FIRE_SLIME_BLOCK;

public class AncientFireSlimyBlock extends AncientSlimyBlock {
    public AncientFireSlimyBlock(Properties properties) {
        super(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
    }
}
