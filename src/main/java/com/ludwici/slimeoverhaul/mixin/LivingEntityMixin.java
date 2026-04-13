package com.ludwici.slimeoverhaul.mixin;

import net.minecraft.core.Holder;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ludwici.slimeoverhaul.Content.*;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

//    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
//    private float handleFriction(BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity) {
//        LivingEntity livingEntity = (LivingEntity) (Object) this;
//        if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder())) {
//            return 0.96f;
//        }
//        return instance.getFriction(levelReader, blockPos, entity);
//    }
//
//    @Inject(method = "hurt", at = @At("RETURN"))
//    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
//        if (cir.getReturnValue() && source.is(DamageTypeTags.IS_FIRE)) {
//            LivingEntity livingEntity = (LivingEntity) (Object) this;
//            if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder())) {
//                livingEntity.removeEffect(SLIPPERY_EFFECT.getHolder());
//            }
//        }
//    }

    @Shadow
    public abstract boolean hasEffect(Holder<MobEffect> effect);

    @Inject(method = "getEffectiveGravity", at = @At("HEAD"), cancellable = true)
    private void calculateGravity(CallbackInfoReturnable<Double> cir) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        boolean needBounce = this.hasEffect(WATER_ANTI_DEPTH_EFFECT.getHolder()) && livingEntity.isEyeInFluid(FluidTags.WATER);
        if (needBounce) {
            cir.setReturnValue(bounceEntity(livingEntity));
            return;
        }

        needBounce = this.hasEffect(LAVA_ANTI_DEPTH_EFFECT.getHolder()) && livingEntity.isEyeInFluid(FluidTags.LAVA);
        if (needBounce) {
            cir.setReturnValue(bounceEntity(livingEntity));
            return;
        }
    }

    @Unique
    private double bounceEntity(LivingEntity entity) {
        Vec3 mov = entity.getDeltaMovement();
        return -(mov.y + (0.2 * 6 - mov.y) * 0.8);
    }

}