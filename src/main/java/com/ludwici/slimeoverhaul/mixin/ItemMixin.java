package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.effect.HandBurnEffect;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ludwici.slimeoverhaul.Content.HAND_BURN_EFFECT;

@Mixin(ItemStack.class)
public class ItemMixin {

    @Inject(method = "useOn", at = @At("RETURN"))
    private void burnHands(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
//        if (cir.getReturnValue().indicateItemUse()) {
//            Player player = context.getPlayer();
//            var effect = player.getEffect(HAND_BURN_EFFECT.getHolder());
//
//            if (effect != null) {
//                player.hurt(player.level().damageSources().inFire(), HandBurnEffect.getDamage(effect.getAmplifier()));
//            }
//        }
    }

}
