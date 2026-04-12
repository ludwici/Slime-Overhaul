package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.entity.custom.elementals.WaterSlime;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
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
    protected void checkWaterSlimeInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise, CallbackInfo ci) {
        if (entity instanceof WaterSlime slime) {
            level.removeBlock(pos, true);
            if (slime.isTiny()) {
                if (!level.isClientSide()) {
                    slime.kill((ServerLevel) level);
                }
            }
            slime.fireBlockPos = null;
        }
    }
}
