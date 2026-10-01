package net.id.paradise_lost.services;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.id.paradise_lost.networking.packet.C2SModPacket;
import net.id.paradise_lost.networking.packet.S2CModPacket;
import net.id.paradise_lost.platform.services.INetworkHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.api.EnvType;

@SuppressWarnings("UnstableApiUsage")
public class FabricNetworkHelper implements INetworkHelper {
    @Override
    public <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {

        PayloadTypeRegistry.playS2C().register(type, streamCodec);
        if (isPhysicalClient()) {
            ClientPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handleClient());
        }
    }

    @Override
    public <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        PayloadTypeRegistry.playC2S().register(type, streamCodec);
        ServerPlayNetworking.registerGlobalReceiver(type, (payload, context) -> payload.handleServer(context.player()));
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        ServerPlayNetworking.send(player, msg);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        ClientPlayNetworking.send(msg);
    }

    private static boolean isPhysicalClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
