package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.entity.custom.elementals.WaterSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ColoredFallingBlock;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;

import static com.ludwici.slimeoverhaul.Content.SLIPPERY_EFFECT;
import static com.ludwici.slimeoverhaul.config.Config.WATER_SLIME_CAN_TRANSFORM_SAND;

@Mixin(ColoredFallingBlock.class)
public abstract class ColoredFallingBlockMixin extends FallingBlock {
    public ColoredFallingBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (state.is(Blocks.SAND) && entity instanceof LivingEntity livingEntity) {
            if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder()) || (livingEntity instanceof WaterSlime slime && WATER_SLIME_CAN_TRANSFORM_SAND.isTrue() && !slime.isTiny())) {
                level.setBlock(pos, Blocks.MUD.defaultBlockState(), 3);
            }
        }
    }
}
