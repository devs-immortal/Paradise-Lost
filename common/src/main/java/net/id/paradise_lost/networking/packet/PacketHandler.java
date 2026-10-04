package net.id.paradise_lost.networking.packet;

import net.id.paradise_lost.platform.Services;
import net.minecraft.server.level.ServerPlayer;

public final class PacketHandler {

    private PacketHandler() {}

    public static void registerPackets() {
        Services.NETWORK.registerServerPlayPacket(FloatyAnchorC2SPacket.TYPE, FloatyAnchorC2SPacket.CODEC);
    }


    public static void sendToServer(C2SModPacket<?> packet) {
        Services.NETWORK.sendToServer(packet);
    }

    public static void sendToClient(S2CModPacket<?> packet, ServerPlayer player) {
        Services.NETWORK.sendToClient(packet, player);
    }
}
