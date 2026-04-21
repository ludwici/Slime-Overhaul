package com.ludwici.slimeoverhaul.mixin;

import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import com.ludwici.slimeoverhaul.datamap.UpgradeCost;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.Item;
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

        Holder<Item> holder = template.getItemHolder();
        UpgradeCost data = holder.getData(UPGRADE_COST_DATA);

        if (data == null) {
            return;
        }

        ci.cancel();
        ItemStack weapon = inputSlots.getItem(1);
        if (!weapon.is(ItemTags.SWORDS)) {
            return;
        }

        ItemStack material = inputSlots.getItem(2);

        if (!material.is(data.itemStack().getItem())) {
            return;
        }

        ItemStack result = weapon.copy();
        PyrocideLevel pl = result.get(PYROCIDE_LEVEL.get());
        if (pl != null) {
            return;
        }

        PyrocideLevel pyrocideLevel = new PyrocideLevel();
        result.set(PYROCIDE_LEVEL.get(), pyrocideLevel);

        resultSlots.setItem(0, result);
        //TODO: fix dupe if slot template and material non empty
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    protected void checkResult(Player player, boolean hasStack, CallbackInfoReturnable<Boolean> cir) {
        if (!hasStack) {
            return;
        }

        ItemStack template = inputSlots.getItem(0);
        if (template.is(FIRE_TEMPLATE.get())) {
            cir.setReturnValue(true);
        }
    }
}
