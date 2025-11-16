package com.ludwici.slimeoverhaul.block.slimy;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.Blocks;

public class AncientWaterSlimyBlock extends AncientSlimyBlock{
    public AncientWaterSlimyBlock(Properties properties) {
        super(Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS, SoundEvents.BRUSH_GRAVEL, SoundEvents.BRUSH_GRAVEL_COMPLETED, properties);
        blockBehaviourList.add(new SlimyBlockBehaviour.Water());
    }
}
