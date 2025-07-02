package net.id.paradiselost.mixin.client.network;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.client.rendering.util.ParadiseLostEvents;
import net.id.paradiselost.client.sound.LevitaMovingMinecartSoundInstance;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonNetworkHandler {

    @Shadow
    private ClientWorld world;

    public ClientPlayNetworkHandlerMixin(MinecraftClient client, ClientConnection connection, ClientConnectionState connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "playSpawnSound", at = @At("HEAD"))
    private void playSpawnSound(Entity entity, CallbackInfo ci) {
        if (entity instanceof AbstractMinecartEntity abstractMinecartEntity) {
            this.client.getSoundManager().play(new LevitaMovingMinecartSoundInstance(abstractMinecartEntity, false));
        }
    }

    @Inject(method = "onEntityStatus", at = @At("RETURN"))
    public void onEntityStatus(EntityStatusS2CPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.world);
        if (entity != null && packet.getStatus() == ParadiseLostEvents.LEVITATION_TOTEM_USED) {
            this.client.particleManager.addEmitter(entity, ParadiseLostParticles.LEVITATION_TOTEM, 30);
            this.world.playSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
            if (entity == this.client.player) {
                this.client.gameRenderer.showFloatingItem(ParadiseLostItems.TOTEM_OF_LEVITATION.getDefaultStack());
            }
        }

    }

}
