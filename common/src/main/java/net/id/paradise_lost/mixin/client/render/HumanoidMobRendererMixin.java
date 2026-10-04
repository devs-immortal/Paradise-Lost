package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.client.rendering.entity.state.FloatyAnchoredRenderState;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidMobRenderer.class)
public class HumanoidMobRendererMixin {
    @Inject(method = "extractHumanoidRenderState", at = @At("TAIL"))
    private static void paradiseLost$extractFloatyAnchored(LivingEntity entity, HumanoidRenderState state, float partialTick, CallbackInfo ci) {
        ((FloatyAnchoredRenderState) state).paradiseLost$setFloatyAnchored(
                entity instanceof ParadiseLostEntityExtensions extensions && extensions.isFloatyAnchored());
    }
}
