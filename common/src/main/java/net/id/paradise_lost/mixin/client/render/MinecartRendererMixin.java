package net.id.paradise_lost.mixin.client.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.id.paradise_lost.attachments.MinecartFloating;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(MinecartRenderer.class)
public class MinecartRendererMixin {

    @ModifyVariable(method = "render", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float paradiseLost$floatRenderYaw(float entityYaw, AbstractMinecart entity) {
        if (MinecartFloating.shouldUseFloatRenderPose(entity)) {
            return MinecartFloating.getFloatRenderYaw(entity);
        }
        return entityYaw;
    }

    @ModifyExpressionValue(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(FFF)F"
            )
    )
    private float paradiseLost$floatRenderPitch(float lerpedPitch, AbstractMinecart entity) {
        if (MinecartFloating.shouldUseFloatRenderPose(entity)) {
            return MinecartFloating.getFloatRenderPitch(entity);
        }
        return lerpedPitch;
    }
}
