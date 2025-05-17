package com.ludwici.slimeoverhaul.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = MODID)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar(MODID).versioned("1.0.0").optional();
        registrar.playToServer(KeyPressPacket.TYPE, KeyPressPacket.STREAM_CODEC, KeyPressPacket::handle);
    }
}
