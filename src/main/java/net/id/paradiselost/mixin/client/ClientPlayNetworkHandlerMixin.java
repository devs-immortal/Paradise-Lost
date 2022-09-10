package net.id.paradiselost.mixin.client;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.id.paradiselost.world.ParadiseLostGameRules;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.NetworkThreadUtils;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Final
    @Shadow
    private MinecraftClient client;

    @Inject(method = "onCustomPayload", at = @At("HEAD"), cancellable = true)
    private void onCustomPayload(CustomPayloadS2CPacket packet, CallbackInfo ci) {
        // This seems quite hacky.
        if (packet.getChannel().equals(ParadiseLostGameRules.MAX_QUICKSOIL_SPEED_ID)) {
            NetworkThreadUtils.forceMainThread(packet, ((ClientPlayNetworkHandler) (Object) this), this.client);
            ((ClientPlayNetworkHandler) (Object) this).getWorld()
                    .getGameRules()
                    .get(ParadiseLostGameRules.MAX_QUICKSOIL_SPEED)
                    .setValue(GameRuleFactory.createDoubleRule(packet.getData().readDouble()).createRule(),
                            (((ClientPlayNetworkHandler) (Object) this).getWorld().getServer()));
            ci.cancel();
        }
    }
}
