package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.entities.ParadiseLostEntityExtensions;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
    @Inject(method = "setupTransforms", at = @At("TAIL"))
    private void setupTransforms(LivingEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta, CallbackInfo ci) {
        if (((ParadiseLostEntityExtensions) entity).getFlipped()) {
            if (!(entity instanceof PlayerEntity)) {
                matrices.translate(0.0D, entity.getHeight() + 0.1F, 0.0D);
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
            }
        }
    }
}
