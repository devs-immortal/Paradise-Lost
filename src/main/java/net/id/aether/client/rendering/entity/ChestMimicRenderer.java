package net.id.aether.client.rendering.entity;

import net.id.aether.Aether;
import net.id.aether.client.model.entity.ChestMimicModel;
import net.id.aether.client.rendering.entity.layer.AetherModelLayers;
import net.id.aether.entities.hostile.ChestMimicEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ChestMimicRenderer extends MobEntityRenderer<ChestMimicEntity, ChestMimicModel> {

    private static final Identifier TEXTURE_HEAD = Aether.locate("textures/entity/mimic/mimic_head.png");
    private static final Identifier TEXTURE_LEGS = Aether.locate("textures/entity/mimic/mimic_legs.png");
    private static final Identifier TEXTURE_HEAD_XMAS = Aether.locate("textures/entity/mimic/mimic_head_christmas.png");
    private static final Identifier TEXTURE_LEGS_XMAS = Aether.locate("textures/entity/mimic/mimic_legs_christmas.png");

    public ChestMimicRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ChestMimicModel(renderManager.getPart(AetherModelLayers.MIMIC)), 0.0F);
    }

    @Override
    public void render(ChestMimicEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();
        matrices.translate(entity.getX(), entity.getY(), entity.getZ());
        // TODO: Fix rotate call (1.17)
        //GlStateManager.rotatef(180.0F - entity.getPitch(), 0.0F, 1.0F, 0.0F);
        matrices.scale(-1.0F, -1.0F, 1.0F);

        this.model.setAngles(entity, 0, 0F, 0.0F, 0.0F, 0.0F);

        // TODO: FIXME Please!
        /*Calendar calendar = Calendar.getInstance();
        if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
        {
            this.bindTexture(TEXTURE_HEAD_XMAS);
            this.modelbase.renderHead(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mimic);

            this.bindTexture(TEXTURE_LEGS_XMAS);
            this.modelbase.renderLegs(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mimic);
        }
        else
        {
            this.bindTexture(TEXTURE_HEAD);
            this.model.render(matrices, null, light, 0);
            this.modelbase.renderHead(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mimic);
            this.bindTexture(TEXTURE_LEGS);
            this.modelbase.renderLegs(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, mimic);
        }*/

        matrices.pop();
    }

    @Override
    public Identifier getTexture(ChestMimicEntity entity) {
        return null;
    }
}
