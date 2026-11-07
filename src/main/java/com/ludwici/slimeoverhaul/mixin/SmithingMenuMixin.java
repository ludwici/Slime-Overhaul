package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import com.ludwici.slimeoverhaul.datamap.UpgradeCost;
import com.ludwici.slimeoverhaul.datamap.UpgradeCosts;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static com.ludwici.slimeoverhaul.Content.*;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    public SmithingMenuMixin(@Nullable MenuType<?> type, int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(type, containerId, playerInventory, access);
    }

    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    protected void createCustomTemplateResult(CallbackInfo ci) {
        ItemStack template = inputSlots.getItem(0);
        if (!template.is(SMITHING_TEMPLATES_TAG)) {
            return;
        }

        UpgradeCost data = UpgradeCosts.get(template);
        if (data == null) {
            return;
        }

        ci.cancel();
        ItemStack weapon = inputSlots.getItem(1);
        if (!weapon.is(ItemTags.SWORDS)) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        ItemStack material = inputSlots.getItem(2);
        if (!material.is(data.itemStack().getItem())) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        ItemStack result = weapon.copy();
        if (result.has(PYROCIDE_LEVEL.get())) {
            resultSlots.setItem(0, ItemStack.EMPTY);
            return;
        }

        result.set(PYROCIDE_LEVEL.get(), new PyrocideLevel());
        resultSlots.setItem(0, result);
        broadcastChanges();
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    protected void checkResult(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        if (hasStack && inputSlots.getItem(0).is(FIRE_TEMPLATE.get())) {
            cir.setReturnValue(true);
        }
    }
}
