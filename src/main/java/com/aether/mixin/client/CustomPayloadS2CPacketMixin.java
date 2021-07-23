package com.aether.mixin.client;

import com.aether.world.AetherGameRules;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomPayloadS2CPacket.class)
public class CustomPayloadS2CPacketMixin {
    @Inject(method = "apply", at = @At("HEAD"), cancellable = true)
    private void handleAetherPackets(ClientPlayPacketListener listener, CallbackInfo ci) {
        if (((CustomPayloadS2CPacket) (Object) this).getChannel().equals(AetherGameRules.MAX_QUICKSOIL_SPEED_ID)) {
            ((ClientPlayNetworkHandler) listener).getWorld()
                    .getGameRules()
                    .get(AetherGameRules.MAX_QUICKSOIL_SPEED)
                    .setValue(GameRuleFactory.createDoubleRule(((CustomPayloadS2CPacket) (Object) this).getData().readDouble()).createRule(),
                            ((ClientPlayNetworkHandler) listener).getWorld().getServer());
            ci.cancel();
        }
    }
}
