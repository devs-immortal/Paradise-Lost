package net.id.paradise_lost.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.rendering.entity.state.SentinelEntityRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

public class SentinelEyesFeatureRenderer extends EyesLayer<SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> {
    private static final RenderType TEXTURE = RenderType.eyes(ModConstants.id("textures/entity/sentinel/sentinel_eyes.png"));

    public SentinelEyesFeatureRenderer(RenderLayerParent<SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, SentinelEntityRenderState state, float yRot, float xRot) {
        if (state.enlightened) {
            super.render(matrices, vertexConsumers, light, state, yRot, xRot);
        }
    }

    @Override
    public RenderType renderType() {
        return TEXTURE;
    }
}
