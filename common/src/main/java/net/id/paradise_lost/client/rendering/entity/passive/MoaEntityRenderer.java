package net.id.paradise_lost.client.rendering.entity.passive;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.MoaModel;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MoaEntityRenderer extends MobRenderer<MoaEntity, MoaModel> {

    public MoaEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MoaModel(renderManager.bakeLayer(ParadiseLostModelLayers.MOA)), 0.7f);
    }

    @Override
    protected void scale(MoaEntity moa, PoseStack matrixStack, float partialTicks) {
        float moaScale = moa.isBaby() ? Math.min(0.43F + (moa.tickCount * 0.00001f), 0.72f) : 1.0F;
        matrixStack.scale(moaScale, moaScale, moaScale);
    }

    @Override
    public void render(MoaEntity moa, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        super.render(moa, f, g, matrixStack, vertexConsumerProvider, moa.getGenes().getRace().glowing() ? LightTexture.FULL_BRIGHT : i);
    }

    @Override
    public ResourceLocation getTextureLocation(MoaEntity entity) {
        return entity.getGenes().getTexture();
    }
}
