package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.entity.custom.elementals.WaterSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BaseFireBlock.class)
public abstract class BaseFireBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"))
    protected void checkWaterSlimeInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity, CallbackInfo ci) {
        if (entity instanceof WaterSlime slime) {
            level.removeBlock(blockPos, true);
            if (slime.isTiny()) {
                slime.kill();
            }
            slime.fireBlockPos = null;
        }
    }
}
