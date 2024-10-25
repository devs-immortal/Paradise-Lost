package net.id.paradiselost.mixin.client.render;

import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.PlayerHeldItemFeatureRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerHeldItemFeatureRenderer.class)
public abstract class PlayerHeldItemFeatureRendererMixin {

    @Inject(method = "renderItem", at = @At("HEAD"), cancellable = true)
    public void renderItem(LivingEntity entity, ItemStack stack, ModelTransformationMode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        if (stack.isOf(ParadiseLostItems.OLVITE_SPYGLASS) && entity.getActiveItem() == stack && entity.handSwingTicks == 0) {
            this.renderSpyglass(entity, stack, arm, matrices, vertexConsumers, light);
            ci.cancel();
        }
    }

    @Shadow
    protected abstract void renderSpyglass(LivingEntity entity, ItemStack stack, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light);

}
