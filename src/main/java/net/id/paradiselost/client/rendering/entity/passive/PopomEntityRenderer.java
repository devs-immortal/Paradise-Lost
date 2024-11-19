package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.PopomEntityModel;
import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PopomEntityRenderer extends MobEntityRenderer<PopomEntity, PopomEntityModel<PopomEntity>> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/popom/popom.png");

    public PopomEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PopomEntityModel<>(renderManager.getPart(ParadiseLostModelLayers.POPOM)), 0.7F);
    }

    public void render(PopomEntity popomEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        this.model.furSize = popomEntity.getFurSize();
        matrixStack.pop();
        super.render(popomEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Identifier getTexture(PopomEntity entity) {
        return TEXTURE;
    }
}