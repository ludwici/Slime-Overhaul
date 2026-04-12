package com.ludwici.slimeoverhaul.client;

import com.ludwici.slimeoverhaul.SlimeOverhaulMod;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;

import static com.ludwici.slimeoverhaul.Content.DOUBLE_JUMP_EFFECT;

@EventBusSubscriber(modid = SlimeOverhaulMod.MODID, value = Dist.CLIENT)
public class KeyRegistry {
    private static boolean canDoubleJump = true;

    @SubscribeEvent
    private static void handleKeyBindings(InputEvent.Key event) {
        var minecraft = Minecraft.getInstance();

        if (minecraft.player == null) {
            return;
        }

        if (event.getAction() == InputConstants.PRESS) {
            if (event.getKey() == minecraft.options.keyJump.getKey().getValue()) {
                if (canDoubleJump) {
                    var player = minecraft.player;
                    if (!player.isCreative() && !player.isSpectator() && !player.onGround() && !player.onClimbable() && !player.isInWater() && !player.isPassenger()) {
                        ItemStack chestItemStack = player.getItemBySlot(EquipmentSlot.CHEST);
                        if (!chestItemStack.has(DataComponents.GLIDER)) {{
                            if (player.hasEffect(DOUBLE_JUMP_EFFECT.getHolder())) {
                                minecraft.player.jumpFromGround();
                                canDoubleJump = false;
                            }
                        }}
                    }
                }
            }
        }

        if (minecraft.player.onGround()) {
            canDoubleJump = true;
        }
    }
}
