package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.CockatriceModel;
import com.aether.entities.hostile.CockatriceEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CockatriceRenderer extends MobEntityRenderer<CockatriceEntity, CockatriceModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/cockatrice/cockatrice.png");

    public CockatriceRenderer(EntityRenderDispatcher rendermanagerIn) {
        super(rendermanagerIn, new CockatriceModel(), 1.0F);
    }

    @Override
    protected float getAnimationProgress(CockatriceEntity cockatrice, float f) {
        float f1 = cockatrice.prevWingRotation + (cockatrice.wingRotation - cockatrice.prevWingRotation) * f;
        float f2 = cockatrice.prevDestPos + (cockatrice.destPos - cockatrice.prevDestPos) * f;

        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    @Override
    protected void scale(CockatriceEntity cockatrice, MatrixStack matrices, float f) {
        matrices.scale(1.8F, 1.8F, 1.8F);
    }

    @Override
    public Identifier getTexture(CockatriceEntity cockatrice) {
        return TEXTURE;
    }
}
