package net.id.paradise_lost.mixin.client.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.id.paradise_lost.client.rendering.entity.state.FloatyMinecartRenderState;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.client.renderer.entity.AbstractMinecartRenderer;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartRenderer.class)
public class MinecartRendererMixin {

    @Inject(
            method = "extractRenderState(Lnet/minecraft/world/entity/vehicle/AbstractMinecart;Lnet/minecraft/client/renderer/entity/state/MinecartRenderState;F)V",
            at = @At("TAIL")
    )
    private void paradiseLost$captureRenderCart(AbstractMinecart entity, MinecartRenderState state, float partialTick, CallbackInfo ci) {
        ((FloatyMinecartRenderState) state).paradiseLost$setCart(entity, partialTick);
    }

    /**
     * First rotationDegrees call: {@code 180 - yRot} (offs may already have set yaw).
     */
    @ModifyArg(
            method = "oldRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 0
            ),
            index = 0
    )
    private static float paradiseLost$forceFloatYawDegrees(float degrees, @Local(argsOnly = true) MinecartRenderState state) {
        FloatyMinecartRenderState floaty = (FloatyMinecartRenderState) state;
        AbstractMinecart entity = floaty.paradiseLost$getCart();
        if (entity == null) {
            return degrees;
        }
        float wanted = MinecartFloatingHelper.shouldUseFloatRenderPose(entity)
                ? 180.0F - MinecartFloatingHelper.getFloatRenderYaw(entity)
                : degrees;
        return MinecartFloatingHelper.smoothRenderYawDegrees(entity, wanted, floaty.paradiseLost$getPartialTick());
    }

    /**
     * Second call: {@code -xRot}.
     */
    @ModifyArg(
            method = "oldRender",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            ),
            index = 0
    )
    private static float paradiseLost$forceFloatPitchDegrees(float degrees, @Local(argsOnly = true) MinecartRenderState state) {
        FloatyMinecartRenderState floaty = (FloatyMinecartRenderState) state;
        AbstractMinecart entity = floaty.paradiseLost$getCart();
        if (entity == null) {
            return degrees;
        }
        float wanted = degrees;
        if (MinecartFloatingHelper.shouldUseFloatRenderPose(entity)) {
            wanted = -MinecartFloatingHelper.getFloatRenderPitch(entity);
            float t = floaty.paradiseLost$getPartialTick();
            Float scale = MinecartFloatingHelper.railSlopeScale(entity,
                    Mth.lerp(t, entity.xOld, entity.getX()),
                    Mth.lerp(t, entity.yOld, entity.getY()),
                    Mth.lerp(t, entity.zOld, entity.getZ()));
            if (scale != null) {
                wanted *= scale;
            }
        }
        return wanted * MinecartFloatingHelper.smoothRenderPitchSign(entity);
    }
}
