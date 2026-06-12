package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.rendering.entity.state.SentinelEntityRenderState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class SentinelEyesFeatureRenderer extends EyesFeatureRenderer<SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> {

    private static final RenderLayer TEXTURE = RenderLayer.getEyes(ParadiseLost.locate("textures/entity/sentinel/sentinel_eyes.png"));

    public SentinelEyesFeatureRenderer(FeatureRendererContext<SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SentinelEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.enlightened) {
            super.render(matrices, vertexConsumers, light, state, limbAngle, limbDistance);
        }
    }

    @Override
    public RenderLayer getEyesTexture() {
        return TEXTURE;
    }
}
