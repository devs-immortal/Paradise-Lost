package net.id.paradise_lost.util;

import net.id.paradise_lost.networking.packet.AnchoredRunMidairS2CPacket;
import net.id.paradise_lost.networking.packet.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;

import static net.id.paradise_lost.world.ParadiseLostGameRules.RUN_MIDAIR;

public final class GameRuleHelper {
    public static void syncAnchoredRun(ServerPlayer player) {
        PacketHandler.sendToClient(new AnchoredRunMidairS2CPacket(player.server.getGameRules().getBoolean(RUN_MIDAIR)), player);
    }

    public static void onAnchoredRunChanged(MinecraftServer server, GameRules.BooleanValue value) {
        if (server == null) {
            return;
        }
        AnchoredRunMidairS2CPacket packet = new AnchoredRunMidairS2CPacket(value.get());
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            PacketHandler.sendToClient(packet, player);
        }
    }
}
