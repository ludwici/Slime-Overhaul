package com.ludwici.slimeoverhaul.network;

import com.ludwici.slimeoverhaul.PlayerKeyPressObserver;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static com.ludwici.slimeoverhaul.SlimeOverhaulMod.MODID;

public class KeyPressPacket implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<KeyPressPacket> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "key_press"));
    public static final StreamCodec<RegistryFriendlyByteBuf, KeyPressPacket> STREAM_CODEC = CustomPacketPayload.codec(KeyPressPacket::write, KeyPressPacket::new);

    public KeyPressPacket() {

    }

    public KeyPressPacket(FriendlyByteBuf buf) {
    }

    public void write(FriendlyByteBuf buf) {

    }

    public static void handle(KeyPressPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player) {
                ((PlayerKeyPressObserver) player).setJumpKeyPressed(true);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
