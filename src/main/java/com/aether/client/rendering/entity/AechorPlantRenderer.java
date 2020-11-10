package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.AechorPlantModel;
import com.aether.entities.hostile.AechorPlantEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AechorPlantRenderer extends MobEntityRenderer<AechorPlantEntity, AechorPlantModel> {

    public static final Identifier TEXTURE = Aether.locate("textures/entity/aechor_plant/aechor_plant.png");

    public AechorPlantRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new AechorPlantModel(), 0.3F);
    }

    @Override
    protected float getAnimationProgress(AechorPlantEntity livingEntity_1, float float_1) {
        return super.getAnimationProgress(livingEntity_1, float_1);
    }

    @Override
    protected void scale(AechorPlantEntity b1, MatrixStack matrices, float f) {
        float f1 = (float) Math.sin(b1.sinage);
        float f3;

        if (b1.hurtTime > 0) {
            f1 *= 0.45F;
            f1 -= 0.125F;
            f3 = 1.75F + (float) Math.sin(b1.sinage + 2.0F) * 1.5F;
        } else {
            f1 *= 0.125F;
            f3 = 1.75F;
        }

        this.getModel().sinage = f1;
        this.getModel().sinage2 = f3;
        float f2 = 0.625F + (float) b1.size / 6.0F;
        this.getModel().size = f2;
        this.shadowOpacity = f2 - 0.25F + f1;
    }

    /*protected int a(AechorPlantEntity AechorPlantEntity, int i, float f)
    {
        if (i != 0)
        {
            return -1;
        }
        else
        {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.325F);
            return 1;
        }
    }

    protected int shouldRenderPass(AechorPlantEntity entityliving, int i, float f)
    {
        return this.a(entityliving, i, f);
    }*/

    @Override
    public Identifier getTexture(AechorPlantEntity entity) {
        return TEXTURE;
    }
}
