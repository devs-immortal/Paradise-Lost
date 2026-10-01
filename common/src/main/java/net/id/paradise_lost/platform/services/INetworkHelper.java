package net.id.paradise_lost.platform.services;

import net.id.paradise_lost.networking.packet.C2SModPacket;
import net.id.paradise_lost.networking.packet.S2CModPacket;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {

    <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec);

    <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec);

    void sendToClient(S2CModPacket<?> msg, ServerPlayer player);

    void sendToServer(C2SModPacket<?> msg);
}
