package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import com.aether.client.model.FlyingCowModel;
import com.aether.entities.passive.FlyingCowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FlyingCowSaddleLayer extends FeatureRenderer<FlyingCowEntity, FlyingCowModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/flying_cow/saddle.png");

    private final FlyingCowModel cowModel = new FlyingCowModel(0.5F);

    public FlyingCowSaddleLayer(FeatureRendererContext<FlyingCowEntity, FlyingCowModel> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, FlyingCowEntity cow, float limbAngle, float limbDistance, float tickDelta, float customAngle, float netHeadYaw, float headPitch) {
        if (cow.getSaddled()) {
            this.cowModel.copyStateTo(this.getContextModel());
            VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
            this.cowModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // TODO: ???
    /*@Override
    public boolean hasHurtOverlay()
    {
        return false;
    }*/

}
