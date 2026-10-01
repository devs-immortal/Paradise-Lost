package net.id.paradise_lost.mixin.client.network;

import net.id.paradise_lost.particle.ParadiseLostParticleTypes;
import net.id.paradise_lost.client.rendering.util.ParadiseLostEvents;
import net.id.paradise_lost.client.sound.LevitaMovingMinecartSoundInstance;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundEntityEventPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPlayNetworkHandlerMixin extends ClientCommonPacketListenerImpl {

    @Shadow
    private ClientLevel level;

    public ClientPlayNetworkHandlerMixin(Minecraft client, Connection connection, CommonListenerCookie connectionState) {
        super(client, connection, connectionState);
    }

    @Inject(method = "postAddEntitySoundInstance", at = @At("HEAD"))
    private void playSpawnSound(Entity entity, CallbackInfo ci) {
        if (entity instanceof AbstractMinecart abstractMinecartEntity) {
            this.minecraft.getSoundManager().play(new LevitaMovingMinecartSoundInstance(abstractMinecartEntity, false));
        }
    }

    @Inject(method = "handleEntityEvent", at = @At("RETURN"))
    public void onEntityStatus(ClientboundEntityEventPacket packet, CallbackInfo ci) {
        Entity entity = packet.getEntity(this.level);
        if (entity != null) {
            if (packet.getEventId() == ParadiseLostEvents.LEVITATION_TOTEM_USED) {
                this.minecraft.particleEngine.createTrackingEmitter(entity, ParadiseLostParticleTypes.LEVITATION_TOTEM, 30);
                this.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
                if (entity == this.minecraft.player) {
                    this.minecraft.gameRenderer.displayItemActivation(ItemRegistry.TOTEM_OF_LEVITATION.get().getDefaultInstance());
                }
            }
        }
    }

}
