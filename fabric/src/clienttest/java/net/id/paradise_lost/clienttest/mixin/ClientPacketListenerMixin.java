package net.id.paradise_lost.clienttest.mixin;

import net.id.paradise_lost.clienttest.Recorder;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleExplosion", at = @At("TAIL"))
    private void clienttest$countExplosion(ClientboundExplodePacket packet, CallbackInfo ci) {
        Recorder.explosions.add(new Vec3(packet.getX(), packet.getY(), packet.getZ()));
    }
}
