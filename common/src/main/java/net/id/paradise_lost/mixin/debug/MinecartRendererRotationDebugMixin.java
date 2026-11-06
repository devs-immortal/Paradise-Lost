package net.id.paradise_lost.mixin.debug;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.debug.MinecartRotationDebug;
import net.id.paradise_lost.util.MinecartFloatingHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MinecartRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecartRenderer.class)
public abstract class MinecartRendererRotationDebugMixin {
    private static final float THRESHOLD = 0.5F;

    @Inject(method = "render", at = @At("RETURN"))
    private void paradiseLost$logRender(
            AbstractMinecart entity,
            float entityYaw,
            float partialTicks,
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            CallbackInfo ci) {
        if (!MinecartFloatingHelper.isFloating(entity)) {
            return;
        }
        boolean floatPose = MinecartFloatingHelper.shouldUseFloatRenderPose(entity);
        float wanted = floatPose ? MinecartFloatingHelper.getFloatRenderYaw(entity) : Float.NaN;
        float drawnDegrees = MinecartFloatingHelper.renderYawFilterValue(entity);
        boolean moved = MinecartRotationDebug.moved(entity, "renderWanted", wanted, THRESHOLD);
        moved |= MinecartRotationDebug.moved(entity, "renderDrawn", drawnDegrees, THRESHOLD);
        if (!moved) {
            return;
        }
        MinecartRotationDebug.log("Render", entity,
                "partial=" + MinecartRotationDebug.deg(partialTicks)
                        + " useFloatPose=" + floatPose
                        + " wantedYaw=" + MinecartRotationDebug.deg(wanted)
                        + " wantedDegrees=" + MinecartRotationDebug.deg(180.0F - wanted)
                        + " drawnDegrees=" + MinecartRotationDebug.deg(drawnDegrees)
                        + " drawnYaw=" + MinecartRotationDebug.deg(180.0F - drawnDegrees)
                        + " lerpedEntityYaw=" + MinecartRotationDebug.deg(Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot()))
                        + " " + MinecartRotationDebug.state(entity));
    }
}
