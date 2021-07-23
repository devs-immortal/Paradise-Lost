package com.aether.mixin.client;

import com.aether.world.AetherGameRules;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleAetherPackets(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        if (packet.getChannel().equals(AetherGameRules.MAX_QUICKSOIL_SPEED_ID)) {
            NetworkThreadUtils.forceMainThread(packet, ((ClientPlayNetworkHandler) (Object) this), this.client);
            ((ClientPlayNetworkHandler) (Object) this).getWorld()
                    .getGameRules()
                    .get(AetherGameRules.MAX_QUICKSOIL_SPEED)
                    .setValue(GameRuleFactory.createDoubleRule(packet.getData().readDouble()).createRule(),
                            (((ClientPlayNetworkHandler) (Object) this).getWorld().getServer()));
            //}
            ci.cancel();
        }
    }
}