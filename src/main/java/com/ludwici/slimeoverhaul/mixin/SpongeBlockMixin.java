package com.ludwici.slimeoverhaul.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SpongeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import static com.ludwici.slimeoverhaul.Content.SLIPPERY_EFFECT;

@Mixin(SpongeBlock.class)
public abstract class SpongeBlockMixin extends Block {

    public SpongeBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder())) {
                livingEntity.removeEffect(SLIPPERY_EFFECT.getHolder());
            }
        }
    }
}
