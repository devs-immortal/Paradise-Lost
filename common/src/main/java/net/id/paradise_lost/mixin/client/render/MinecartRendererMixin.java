package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.attachments.MinecartFloating;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public class MinecartRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V",
            at = @At("TAIL")
    )
    private void paradiseLost$floatingRailRotation(AbstractMinecart cart, MinecartRenderState state, float partialTick, CallbackInfo ci) {
        if (state.isNewRender || !MinecartFloating.shouldUseFloatingRenderRotation(cart)) {
            return;
        }
        // skip the snap to the exit rail
        state.posOnRail = null;
        state.frontPos = null;
        state.backPos = null;
        Float yaw = MinecartFloating.getFloatingRenderYaw(cart);
        if (yaw != null) {
            state.yRot = yaw;
        }
    }
}
