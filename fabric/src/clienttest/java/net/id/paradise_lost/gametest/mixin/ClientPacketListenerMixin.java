package net.id.paradise_lost.gametest.mixin;

import net.id.paradise_lost.gametest.Recorder;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    @Inject(method = "handleExplosion", at = @At("TAIL"))
    private void clienttest$countExplosion(ClientboundExplodePacket packet, CallbackInfo ci) {
        Recorder.explosions.add(packet.center());
    }
}
