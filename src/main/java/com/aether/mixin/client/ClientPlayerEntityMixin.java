package com.aether.mixin.client;

import com.aether.entities.block.FloatingBlockEntity;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LocalPlayer.class)
public abstract class ClientPlayerEntityMixin implements FloatingBlockEntity.ICPEM {

    @Shadow
    protected abstract void sendPosition();

    @Unique
    boolean sendMovement = false;

    /**
     * Since the player can be moved by FloatingBlockEntity after ClientPlayerEntity.tick()
     * the call to sendMovementPackets() needs to be delayed till after all FloatingBlockEntities have ticked
     */

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;sendPosition()V"), method = "tick")
    void redirectSendMovementPackets(LocalPlayer clientPlayerEntity) {
        sendMovement = true;
    }

    @Override
    public void postTick() {
        if (sendMovement) {
            sendPosition();
            sendMovement = false;
        }
    }
}
