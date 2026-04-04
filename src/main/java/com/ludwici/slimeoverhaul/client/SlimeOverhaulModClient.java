package com.ludwici.slimeoverhaul.client;

import com.ludwici.slimeoverhaul.SlimeOverhaulMod;
import com.ludwici.slimeoverhaul.entity.client.BaseSlimeRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import static com.ludwici.slimeoverhaul.Content.*;
import static com.ludwici.slimeoverhaul.Content.EARTH_SLIME;
import static com.ludwici.slimeoverhaul.Content.FLAME_SLIME;

@Mod(value = SlimeOverhaulMod.MODID, dist = Dist.CLIENT)
@EventBusSubscriber(modid = SlimeOverhaulMod.MODID, value = Dist.CLIENT)
public class SlimeOverhaulModClient {
    public SlimeOverhaulModClient(ModContainer container) {
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(
                CLEANSING_BRUSH.get(),
                ResourceLocation.withDefaultNamespace("brushing"),
                ((stack, level, entity, seed) -> entity != null && entity.getUseItem() == stack
                        ? (float) (entity.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F)
        );
    }

    @SubscribeEvent
    public static void onRendererRegister(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(AIR_SLIME.get(), BaseSlimeRenderer::new);
        event.registerEntityRenderer(WATER_SLIME.get(), BaseSlimeRenderer::new);
        event.registerEntityRenderer(EARTH_SLIME.get(), BaseSlimeRenderer::new);
        event.registerEntityRenderer(FLAME_SLIME.get(), BaseSlimeRenderer::new);
    }
}
