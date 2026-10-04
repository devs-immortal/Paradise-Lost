package net.id.paradise_lost.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.rendering.entity.state.EnvoyEntityRenderState;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;

public class EnvoyEyesFeatureRenderer extends EyesLayer<EnvoyEntityRenderState, SkeletonModel<EnvoyEntityRenderState>> {
    private static final RenderType TEXTURE = RenderType.eyes(ModConstants.id("textures/entity/envoy/envoy_enlightened_eyes.png"));

    public EnvoyEyesFeatureRenderer(RenderLayerParent<EnvoyEntityRenderState, SkeletonModel<EnvoyEntityRenderState>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, EnvoyEntityRenderState state, float yRot, float xRot) {
        if (state.enlightened) {
            super.render(matrices, vertexConsumers, light, state, yRot, xRot);
        }
    }

    @Override
    public RenderType renderType() {
        return TEXTURE;
    }
}
