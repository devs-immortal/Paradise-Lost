package net.id.paradiselost.client.rendering.entity.hostile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.hostile.swet.SwetEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class SwetRenderer extends MobEntityRenderer<SwetEntity, SlimeEntityModel<SwetEntity>> {

    public SwetRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SlimeEntityModel<>(renderManager.getPart(EntityModelLayers.SLIME)), 0.25F);
        addFeature(new SlimeOverlayFeatureRenderer<>(this, renderManager.getModelLoader()));
    }

    @Override
    public void render(SwetEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        shadowRadius = 0.25F * (float) slimeEntity.getSize();
        super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    protected void scale(SwetEntity slimeEntity, MatrixStack matrixStack, float f) {
        float g = 0.999F;
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float h = (float) slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(j * h, 1.0F / j * h, j * h);
    }

    @Override
    public Identifier getTexture(SwetEntity slimeEntity) {
        Identifier id = Registry.ENTITY_TYPE.getId(slimeEntity.getType());
        return new Identifier(id.getNamespace(), "textures/entity/swet/" + id.getPath() + ".png");
    }
}
