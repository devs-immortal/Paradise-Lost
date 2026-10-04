package net.id.paradise_lost.mixin.server;

import net.id.paradise_lost.entity.block.FloatingBlockEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "isPlayerCollidingWithAnythingNew", at = @At("RETURN"), cancellable = true)
    void isPlayerNotCollidingWithBlocks(LevelReader worldView, AABB box, double newX, double newY, double newZ, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            List<Entity> list = player.level().getEntities(player, player.getBoundingBox().inflate(0.0, 0.1, 0.0));
            for (Entity entity : list) {
                if (entity instanceof FloatingBlockEntity) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
}
