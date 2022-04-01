package net.id.aether.mixin.server;

import net.id.aether.entities.block.FloatingBlockEntity;
import net.id.aether.entities.util.CustomInventoryEntity;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Locale;

/**
 * Current changes:
 * - Prevents player from rubber banding when riding blocks
 * - Enable {@link CustomInventoryEntity} to work
 */
@Mixin(ServerPlayNetworkHandler.class)
public abstract class ServerPlayNetworkHandlerMixin {
    @Shadow public ServerPlayerEntity player;
    
    /**
     * Stop the player from rubber banding when riding a floating block
     */
    @Inject(method = "isPlayerNotCollidingWithBlocks", at = @At("RETURN"), cancellable = true)
    void isPlayerNotCollidingWithBlocks(WorldView worldView, Box box, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) {
            List<Entity> list = player.world.getOtherEntities(player, player.getBoundingBox());
            for (Entity entity : list) {
                if (entity instanceof FloatingBlockEntity) {
                    cir.setReturnValue(false);
                }
            }
        }
    }
    
    /**
     * Allow Aether entities to open an inventory screen too.
     */
    @Inject(
        method = "onClientCommand",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/network/ServerPlayerEntity;getVehicle()Lnet/minecraft/entity/Entity;",
            ordinal = 0
        ),
        slice = @Slice(
            from = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/JumpingMount;stopJumping()V"
            ),
            to = @At(
                value = "INVOKE",
                target = "Lnet/minecraft/entity/passive/HorseBaseEntity;openInventory(Lnet/minecraft/entity/player/PlayerEntity;)V"
            )
        ),
        cancellable = true
    )
    private void onClientCommand(ClientCommandC2SPacket packet, CallbackInfo ci) {
        if(player.getVehicle() instanceof CustomInventoryEntity entity){
            entity.openInventory(player);
            ci.cancel();
        }
    }
}
