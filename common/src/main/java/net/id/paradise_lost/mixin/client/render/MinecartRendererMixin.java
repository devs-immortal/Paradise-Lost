package net.id.paradise_lost.mixin.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartRenderer.class)
public class MinecartRendererMixin {

    @Unique
    private static final ThreadLocal<AbstractMinecart> PARADISE_LOST$RENDER_CART = new ThreadLocal<>();

    @Unique
    private static final ThreadLocal<Float> PARADISE_LOST$RENDER_PARTIAL = new ThreadLocal<>();

    @Inject(method = "render", at = @At("HEAD"))
    private void paradiseLost$captureRenderCart(
            AbstractMinecart entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci) {
        PARADISE_LOST$RENDER_CART.set(entity);
        PARADISE_LOST$RENDER_PARTIAL.set(partialTicks);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void paradiseLost$clearRenderCart(CallbackInfo ci) {
        PARADISE_LOST$RENDER_CART.remove();
        PARADISE_LOST$RENDER_PARTIAL.remove();
    }

    /**
     * First rotationDegrees call: {@code 180 - entityYaw} (offs may already have set yaw).
     */
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 0
            ),
            index = 0
    )
    private float paradiseLost$forceFloatYawDegrees(float degrees) {
        AbstractMinecart entity = PARADISE_LOST$RENDER_CART.get();
        if (entity == null) {
            return degrees;
        }
        float wanted = MinecartFloatingHelper.shouldUseFloatRenderPose(entity)
                ? 180.0F - MinecartFloatingHelper.getFloatRenderYaw(entity)
                : degrees;
        Float partial = PARADISE_LOST$RENDER_PARTIAL.get();
        return MinecartFloatingHelper.smoothRenderYawDegrees(entity, wanted, partial == null ? 0.0F : partial);
    }

    /**
     * Second call: {@code -pitch}.
     */
    @ModifyArg(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/math/Axis;rotationDegrees(F)Lorg/joml/Quaternionf;",
                    ordinal = 1
            ),
            index = 0
    )
    private float paradiseLost$forceFloatPitchDegrees(float degrees) {
        AbstractMinecart entity = PARADISE_LOST$RENDER_CART.get();
        if (entity == null) {
            return degrees;
        }
        float wanted = degrees;
        if (MinecartFloatingHelper.shouldUseFloatRenderPose(entity)) {
            wanted = -MinecartFloatingHelper.getFloatRenderPitch(entity);
            Float partial = PARADISE_LOST$RENDER_PARTIAL.get();
            float t = partial == null ? 1.0F : partial;
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
