package com.ludwici.slimeoverhaul.client;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ElytraItem;

import static com.ludwici.slimeoverhaul.Content.DOUBLE_JUMP_EFFECT;

public final class KeyRegistry {
    private static boolean canDoubleJump = true;
    private static boolean wasJumpDown;

    private KeyRegistry() {
    }

    public static void register() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) {
                canDoubleJump = true;
                wasJumpDown = false;
                return;
            }

            boolean jumpDown = client.options.keyJump.isDown();
            var player = client.player;
            if (player.onGround()) {
                canDoubleJump = true;
            }
            if (jumpDown && !wasJumpDown && canDoubleJump && !player.isCreative() && !player.isSpectator()
                    && !player.onGround() && !player.onClimbable() && !player.isInWaterOrBubble()
                    && !ElytraItem.isFlyEnabled(player.getItemBySlot(EquipmentSlot.CHEST)) && !player.isPassenger()
                    && player.hasEffect(DOUBLE_JUMP_EFFECT.getHolder())) {
                player.jumpFromGround();
                canDoubleJump = false;
            }
            wasJumpDown = jumpDown;
        });
    }
}
