package com.aether.client.rendering.entity.layer;

import com.aether.Aether;
import com.aether.entities.passive.PhygEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;

public class PhygWingLayer extends RenderLayer<PhygEntity, PigModel<PhygEntity>> {

    private static final ResourceLocation TEXTURE_WINGS = Aether.locate("textures/entity/phyg/wings.png");
    //private final PhygWingModel model = new PhygWingModel();

    public PhygWingLayer(RenderLayerParent<PhygEntity, PigModel<PhygEntity>> context) {
        super(context);
    }

    @Override
    public void render(PoseStack matrices, MultiBufferSource vertexConsumers, int light, PhygEntity phyg, float limbAngle, float limbDistance, float tickDelta, float customAngle, float netHeadYaw, float headPitch) {
        //VertexConsumer vertexConsumer = vertexConsumers.getBuffer(RenderLayer.getEntityCutoutNoCull(TEXTURE_WINGS));
        //this.model.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    // TODO: ???
    /*@Override
    public boolean hasHurtOverlay()
    {
        return false;
    }*/

}
