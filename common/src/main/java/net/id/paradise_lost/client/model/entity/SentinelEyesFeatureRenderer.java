package net.id.paradise_lost.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.hostile.SentinelEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

public class SentinelEyesFeatureRenderer<T extends SentinelEntity> extends EyesLayer<T, SentinelEntityModel<T>> {

    private static final RenderType TEXTURE = RenderType.eyes(ModConstants.id("textures/entity/sentinel/sentinel_eyes.png"));

    public SentinelEyesFeatureRenderer(RenderLayerParent<T, SentinelEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getEnlightened()) {
            super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    @Override
    public RenderType renderType() {
        return TEXTURE;
    }
}
