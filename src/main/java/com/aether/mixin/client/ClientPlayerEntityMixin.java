package com.aether.mixin.client;

import com.aether.entities.block.FloatingBlockEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin implements FloatingBlockEntity.PostTickEntity {

    @Shadow
    protected abstract void sendMovementPackets();

    @Unique
    boolean sendMovement = false;

    /**
     * Since the player can be moved by FloatingBlockEntity after ClientPlayerEntity.tick()
     * the call to sendMovementPackets() needs to be delayed till after all FloatingBlockEntities have ticked
     */

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayerEntity;sendMovementPackets()V"))
    void redirectSendMovementPackets(ClientPlayerEntity clientPlayerEntity) {
        sendMovement = true;
    }

    @Override
    public void postTick() {
        if (sendMovement) {
            sendMovementPackets();
            sendMovement = false;
        }
    }
}
