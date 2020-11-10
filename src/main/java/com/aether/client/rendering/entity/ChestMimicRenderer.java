package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.ChestMimicModel;
import com.aether.entities.hostile.ChestMimicEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.Calendar;

public class ChestMimicRenderer extends MobEntityRenderer<ChestMimicEntity, ChestMimicModel> {

    private static final Identifier TEXTURE_HEAD = Aether.locate("textures/entity/mimic/mimic_head.png");
    private static final Identifier TEXTURE_LEGS = Aether.locate("textures/entity/mimic/mimic_legs.png");
    private static final Identifier TEXTURE_HEAD_XMAS = Aether.locate("textures/entity/mimic/mimic_head_christmas.png");
    private static final Identifier TEXTURE_LEGS_XMAS = Aether.locate("textures/entity/mimic/mimic_legs_christmas.png");

    public ChestMimicRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new ChestMimicModel(), 0.0F);
    }

    @Override
    public void render(ChestMimicEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        GlStateManager.pushMatrix();
        GlStateManager.translated(entity.getX(), entity.getY(), entity.getZ()); // TODO: ???
        GlStateManager.rotatef(180.0F - entity.pitch, 0.0F, 1.0F, 0.0F);
        GlStateManager.scalef(-1.0F, -1.0F, 1.0F);

        this.model.setAngles(entity, 0, 0F, 0.0F, 0.0F, 0.0F);

        Calendar calendar = Calendar.getInstance();

        // TODO: FIXME Please!
        /*if (calendar.get(2) + 1 == 12 && calendar.get(5) >= 24 && calendar.get(5) <= 26)
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

        GlStateManager.popMatrix();
    }

    @Override
    public Identifier getTexture(ChestMimicEntity entity) {
        return null;
    }
}
