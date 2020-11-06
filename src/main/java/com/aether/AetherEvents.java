package com.aether;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;

public class AetherEvents {
    public static void ServerTickEnd(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach((ServerPlayerEntity player) -> {
            if (player.getY() < 10 && "the_aether".equals(player.world.getRegistryKey().getValue().getPath())) {
                //TODO: Tweak y coordinate and disable fall damage
                player.teleport(server.getWorld(World.OVERWORLD), player.getX(), 200, player.getZ(), player.yaw, player.pitch);
            }
        });
    }
}
