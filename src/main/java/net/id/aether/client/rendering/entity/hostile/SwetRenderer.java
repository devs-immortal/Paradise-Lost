package net.id.aether.client.rendering.entity.hostile;

import net.id.aether.Aether;
import net.id.aether.entities.hostile.swet.BlueSwetEntity;
import net.id.aether.entities.hostile.swet.GoldenSwetEntity;
import net.id.aether.entities.hostile.swet.PurpleSwetEntity;
import net.id.aether.entities.hostile.swet.SwetEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SlimeOverlayFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.SlimeEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class SwetRenderer extends MobEntityRenderer<SwetEntity, SlimeEntityModel<SwetEntity>> {

    public SwetRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SlimeEntityModel<>(renderManager.getPart(EntityModelLayers.SLIME)), 0.25F);
        this.addFeature(new SlimeOverlayFeatureRenderer<>(this, renderManager.getModelLoader()));
    }

    public void render(SwetEntity slimeEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.shadowRadius = 0.25F * (float) slimeEntity.getSize();
        super.render(slimeEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    protected void scale(SwetEntity slimeEntity, MatrixStack matrixStack, float f) {
        float g = 0.999F;
        matrixStack.scale(0.999F, 0.999F, 0.999F);
        matrixStack.translate(0.0D, 0.0010000000474974513D, 0.0D);
        float h = (float) slimeEntity.getSize();
        float i = MathHelper.lerp(f, slimeEntity.lastStretch, slimeEntity.stretch) / (h * 0.5F + 1.0F);
        float j = 1.0F / (i + 1.0F);
        matrixStack.scale(j * h, 1.0F / j * h, j * h);
    }

    public Identifier getTexture(SwetEntity slimeEntity) {
        if (slimeEntity instanceof BlueSwetEntity)
            return Aether.locate("textures/entity/swet/blue_swet.png");
        if (slimeEntity instanceof PurpleSwetEntity)
            return Aether.locate("textures/entity/swet/purple_swet.png");
        if (slimeEntity instanceof GoldenSwetEntity)
            return Aether.locate("textures/entity/swet/golden_swet.png");
        return Aether.locate("textures/entity/swet/white_swet.png");
    }
}
