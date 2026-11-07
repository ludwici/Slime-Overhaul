package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.event.GameplayEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ludwici.slimeoverhaul.Content.KNOCK_BACK_EFFECT;
import static com.ludwici.slimeoverhaul.Content.SLIPPERY_EFFECT;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {
    @Unique
    private float slimeoverhaul$blockedDamage;

    @Redirect(method = "travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;getFriction(Lnet/minecraft/world/level/LevelReader;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)F"))
    private float handleFriction(BlockState instance, LevelReader levelReader, BlockPos blockPos, Entity entity) {
        LivingEntity livingEntity = (LivingEntity) (Object) this;
        if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder())) {
            return 0.96f;
        }
        return instance.getFriction(levelReader, blockPos, entity);
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float slimeoverhaul$modifyDamage(float amount, DamageSource source, float original) {
        return GameplayEvents.modifyIncomingDamage(source, amount);
    }

    @Inject(method = "hurt", at = @At("HEAD"))
    private void slimeoverhaul$captureDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        slimeoverhaul$blockedDamage = amount;
    }

    @Inject(method = "isDamageSourceBlocked", at = @At("RETURN"))
    private void slimeoverhaul$onBlock(DamageSource source, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            GameplayEvents.onShieldBlock((LivingEntity) (Object) this, source, slimeoverhaul$blockedDamage);
        }
    }

    @Inject(method = "knockback", at = @At("HEAD"), cancellable = true)
    private void slimeoverhaul$ignoreKnockback(double strength, double x, double z, CallbackInfo ci) {
        if (((LivingEntity) (Object) this).hasEffect(KNOCK_BACK_EFFECT.getHolder())) {
            ci.cancel();
        }
    }

    @Inject(method = "hurt", at = @At("RETURN"))
    private void onHurt(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() && source.is(DamageTypeTags.IS_FIRE)) {
            LivingEntity livingEntity = (LivingEntity) (Object) this;
            if (livingEntity.hasEffect(SLIPPERY_EFFECT.getHolder())) {
                livingEntity.removeEffect(SLIPPERY_EFFECT.getHolder());
            }
        }
    }
}
