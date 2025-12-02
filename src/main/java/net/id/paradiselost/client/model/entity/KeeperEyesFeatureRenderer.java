package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.entities.hostile.KeeperEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class KeeperEyesFeatureRenderer<T extends KeeperEntity> extends EyesFeatureRenderer<T, KeeperEntityModel<T>> {

    private static final RenderLayer TEXTURE = RenderLayer.getEyes(ParadiseLost.locate("textures/entity/keeper/keeper_eyes.png"));

    public KeeperEyesFeatureRenderer(FeatureRendererContext<T, KeeperEntityModel<T>> featureRendererContext) {
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
