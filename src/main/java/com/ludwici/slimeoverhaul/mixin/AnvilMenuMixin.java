package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.event.GameplayEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public abstract class AnvilMenuMixin {
    @Inject(method = "onTake", at = @At("HEAD"))
    private void slimeoverhaul$onTake(Player player, ItemStack output, CallbackInfo ci) {
        GameplayEvents.onAnvilTake(player, output);
    }
}
