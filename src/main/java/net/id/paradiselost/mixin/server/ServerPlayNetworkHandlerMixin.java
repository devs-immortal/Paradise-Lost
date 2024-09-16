package net.id.paradiselost.mixin.server;

import net.id.paradiselost.entities.block.FloatingBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * Current changes:
 * - Prevents player from rubber banding when riding blocks
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;
    
    /**
     * Stop the player from rubber banding when riding a floating block
     */
    @Inject(method = "isPlayerNotCollidingWithBlocks", at = @At("RETURN"), cancellable = true)
    void isPlayerNotCollidingWithBlocks(WorldView worldView, Box box, double newX, double newY, double newZ, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            List<Entity> list = player.getWorld().getOtherEntities(player, player.getBoundingBox());
            for (Entity entity : list) {
                if (entity instanceof FloatingBlockEntity) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
