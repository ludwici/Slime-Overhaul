package com.ludwici.slimeoverhaul.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ludwici.slimeoverhaul.Content.HAND_BURN_EFFECT;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity {

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "updateIsUnderwater", at = @At("RETURN"))
    private void checkUnderwater(CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue() == Boolean.TRUE && hasEffect(HAND_BURN_EFFECT.getHolder())) {
            removeEffect(HAND_BURN_EFFECT.getHolder());
        }
    }

}
