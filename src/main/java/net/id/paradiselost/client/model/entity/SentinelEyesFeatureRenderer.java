package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.hostile.SentinelEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class SentinelEyesFeatureRenderer<T extends SentinelEntity> extends EyesFeatureRenderer<T, SentinelEntityModel<T>> {

    private static final RenderLayer TEXTURE = RenderLayer.getEyes(ParadiseLost.locate("textures/entity/sentinel/sentinel_eyes.png"));

    public SentinelEyesFeatureRenderer(FeatureRendererContext<T, SentinelEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (entity.getEnlightened()) {
            super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
        }
    }

    @Override
    public RenderLayer getEyesTexture() {
        return TEXTURE;
    }
}
