package net.id.paradiselost.mixin.client.network;

import net.id.paradiselost.client.sound.LevitaMovingMinecartSoundInstance;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.ClientConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    public ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "playSpawnSound", at = @At("HEAD"))
    private void playSpawnSound(Entity entity, CallbackInfo ci) {
        if (entity instanceof AbstractMinecartEntity abstractMinecartEntity) {
            this.client.getSoundManager().play(new LevitaMovingMinecartSoundInstance(abstractMinecartEntity));
        }
    }
}
