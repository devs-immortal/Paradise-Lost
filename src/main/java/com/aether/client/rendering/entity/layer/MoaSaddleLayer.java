package com.aether.client.rendering.entity.layer;

import com.aether.client.model.entity.MoaModel;
import com.aether.client.rendering.entity.MoaRenderer;
import com.aether.entities.passive.MoaEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class MoaSaddleLayer extends FeatureRenderer<MoaEntity, MoaModel> {

    private final MoaRenderer moaRenderer;
    private final MoaModel moaModel = new MoaModel(0.25F);

    public MoaSaddleLayer(MoaRenderer moaRendererIn) {
        super(moaRendererIn);
        this.moaRenderer = moaRendererIn;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, MoaEntity moa, float limbAngle, float limbDistance, float tickDelta, float customAngle, float netHeadYaw, float headPitch) {
        if (moa.getSaddled()) {
            this.moaModel.setAngles(moa, limbAngle, limbDistance, tickDelta, netHeadYaw, headPitch);
            this.moaModel.setAttributes(this.moaRenderer.getModel());
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(moa.getMoaType().getTexture(true)));
            this.moaModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // TODO: ???, Likely no longer exists
    /*@Override
    public boolean hasHurtOverlay()
    {
        return false;
    }*/

}
