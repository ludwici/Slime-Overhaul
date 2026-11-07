package com.ludwici.slimeoverhaul.client;

import com.ludwici.slimeoverhaul.Content;
import com.ludwici.slimeoverhaul.data.PyrocideLevel;
import com.ludwici.slimeoverhaul.entity.client.BaseSlimeRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.ludwici.slimeoverhaul.Content.*;

public final class SlimeOverhaulModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ItemProperties.register(CLEANSING_BRUSH.get(), ResourceLocation.withDefaultNamespace("brushing"),
                (stack, level, entity, seed) -> entity != null && entity.getUseItem() == stack
                        ? (float) (entity.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F);

        EntityRendererRegistry.register(AIR_SLIME.get(), BaseSlimeRenderer::new);
        EntityRendererRegistry.register(WATER_SLIME.get(), BaseSlimeRenderer::new);
        EntityRendererRegistry.register(EARTH_SLIME.get(), BaseSlimeRenderer::new);
        EntityRendererRegistry.register(FLAME_SLIME.get(), BaseSlimeRenderer::new);

        ItemTooltipCallback.EVENT.register((stack, context, type, lines) -> {
            if (Content.isShieldItem(stack)) {
                var data = stack.get(net.minecraft.core.component.DataComponents.CUSTOM_DATA);
                if (data != null) {
                    var tag = data.copyTag();
                    if (tag.contains("bounce")) {
                        lines.add(Component.translatable("slimeoverhaul.earth_power", tag.getInt("bounce")).withStyle(ChatFormatting.GRAY));
                    }
                }
            }
        });

        KeyRegistry.register();
    }
}
