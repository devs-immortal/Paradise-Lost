package com.aether;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;

public class AetherEvents {
    static public void ServerTickEnd(MinecraftServer server) {
        server.getPlayerManager().getPlayerList().forEach((ServerPlayerEntity player) -> {
            if (player.getY() < 10 && "the_aether".equals(player.world.getRegistryKey().getValue().getPath())) {
                //TODO: Teleport to overworld
                //player.teleport(..., 0, 0, 0, 0, 0);
            }
        });
    }

    public static ActionResult UseBlock(PlayerEntity playerEntity, World world, Hand hand, BlockHitResult blockHitResult) {
        if(world.getBlockState(blockHitResult.getBlockPos()).getBlock().is(Blocks.GLOWSTONE) && playerEntity.getMainHandStack().getItem() == Items.WATER_BUCKET) {
            //TODO: Try to create portal
        }
        return ActionResult.PASS;
    }
}
