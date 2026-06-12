package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.rendering.entity.state.EnvoyEntityRenderState;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.SkeletonEntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class EnvoyEyesFeatureRenderer extends EyesFeatureRenderer<EnvoyEntityRenderState, SkeletonEntityModel<EnvoyEntityRenderState>> {

    private static final RenderLayer TEXTURE = RenderLayer.getEyes(ParadiseLost.locate("textures/entity/envoy/envoy_enlightened_eyes.png"));

    public EnvoyEyesFeatureRenderer(FeatureRendererContext<EnvoyEntityRenderState, SkeletonEntityModel<EnvoyEntityRenderState>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, EnvoyEntityRenderState state, float limbAngle, float limbDistance) {
        if (state.enlightened) {
            super.render(matrices, vertexConsumers, light, state, limbAngle, limbDistance);
        }
    }

    @Override
    public RenderLayer getEyesTexture() {
        return TEXTURE;
    }
}
