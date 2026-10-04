package net.id.paradise_lost.services;

import net.id.paradise_lost.networking.packet.C2SModPacket;
import net.id.paradise_lost.networking.packet.S2CModPacket;
import net.id.paradise_lost.platform.services.INetworkHelper;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class NeoForgeNetworkHelper implements INetworkHelper {
    private static PayloadRegistrar registrar;

    public static void setRegistrar(PayloadRegistrar registrar) {
        NeoForgeNetworkHelper.registrar = registrar;
    }

    private static PayloadRegistrar registrar() {
        if (registrar == null) {
            throw new IllegalStateException(
                    "No payload registrar bound; call NeoForgeNetworkHelper.setRegistrar from RegisterPayloadHandlersEvent first");
        }
        return registrar;
    }

    @Override
    public <MSG extends S2CModPacket<?>> void registerClientPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar().playToClient(type, streamCodec, (payload, context) -> payload.handleClient());
    }

    @Override
    public <MSG extends C2SModPacket<?>> void registerServerPlayPacket(CustomPacketPayload.Type<MSG> type, StreamCodec<RegistryFriendlyByteBuf, MSG> streamCodec) {
        registrar().playToServer(type, streamCodec, (payload, context) -> payload.handleServer((ServerPlayer) context.player()));
    }

    @Override
    public void sendToClient(S2CModPacket<?> msg, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    @Override
    public void sendToServer(C2SModPacket<?> msg) {
        PacketDistributor.sendToServer(msg);
    }
}
