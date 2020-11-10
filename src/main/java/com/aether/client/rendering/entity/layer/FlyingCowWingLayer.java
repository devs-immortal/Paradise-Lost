package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import com.aether.client.model.FlyingCowModel;
import com.aether.client.model.FlyingCowWingModel;
import com.aether.entities.passive.FlyingCowEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class FlyingCowWingLayer extends FeatureRenderer<FlyingCowEntity, FlyingCowModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/flying_cow/wings.png");
    private final FlyingCowWingModel model;

    public FlyingCowWingLayer(FeatureRendererContext<FlyingCowEntity, FlyingCowModel> context) {
        super(context);
        this.model = new FlyingCowWingModel();
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, FlyingCowEntity cow, float limbAngle, float limbDistance, float tickDelta, float customAngle, float netHeadYaw, float headPitch) {
        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE));
        this.model.setAngles(cow, limbAngle, limbDistance, customAngle, netHeadYaw, headPitch);
        this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    // TODO: ???
    /*@Override
    public boolean hasHurtOverlay()
    {
        return false;
    }*/

}
