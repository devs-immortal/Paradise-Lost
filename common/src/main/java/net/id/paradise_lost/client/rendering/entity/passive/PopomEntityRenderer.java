package net.id.paradise_lost.client.rendering.entity.passive;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.PopomEntityModel;
import net.id.paradise_lost.entity.passive.PopomEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PopomEntityRenderer extends MobRenderer<PopomEntity, PopomEntityModel<PopomEntity>> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/popom/popom.png");

    public PopomEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PopomEntityModel<>(renderManager.bakeLayer(ParadiseLostModelLayers.POPOM)), 0.7F);
    }

    public void render(PopomEntity popomEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        matrixStack.pushPose();
        this.model.furSize = popomEntity.getFurSize();
        matrixStack.popPose();
        super.render(popomEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public ResourceLocation getTextureLocation(PopomEntity entity) {
        return TEXTURE;
    }
}
