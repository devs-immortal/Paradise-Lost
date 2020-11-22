package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.AerbunnyModel;
import com.aether.entities.passive.AerbunnyEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class AerbunnyRenderer extends MobEntityRenderer<AerbunnyEntity, AerbunnyModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/aerbunny/aerbunny.png");

    public AerbunnyRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new AerbunnyModel(), 0.3F);
    }

    protected void rotateAerbunny(MatrixStack matrices, AerbunnyEntity bunny) {
        if (bunny.getPrimaryPassenger() != null) {
            matrices.translate(0.0F, -0.2F, 0.0F);
        }

        if (!bunny.isOnGround()) {
            if (bunny.getVelocity().y > 0.5D) {
                GlStateManager.rotatef(15.0F, -1.0F, 0.0F, 0.0F);
            } else if (bunny.getVelocity().y < -0.5D) {
                GlStateManager.rotatef(-15.0F, -1.0F, 0.0F, 0.0F);
            } else {
                GlStateManager.rotatef((float) (bunny.getVelocity().y * 30.0D), -1.0F, 0.0F, 0.0F);
            }
        }
    }

    @Override
    protected void scale(AerbunnyEntity entitybunny, MatrixStack matrices, float f) {
        this.rotateAerbunny(matrices, entitybunny);
    }

    @Override
    public Identifier getTexture(AerbunnyEntity entity) {
        return TEXTURE;
    }
}
