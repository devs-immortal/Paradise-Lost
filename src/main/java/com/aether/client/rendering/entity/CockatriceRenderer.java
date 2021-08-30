package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.CockatriceModelNew;
import com.aether.client.rendering.entity.layer.AetherModelLayers;
import com.aether.entities.hostile.CockatriceEntity;

import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class CockatriceRenderer extends MobEntityRenderer<CockatriceEntity, CockatriceModelNew> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/cockatrice.png");

    public CockatriceRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CockatriceModelNew(renderManager.getPart(AetherModelLayers.COCKATRICE)), 1.0F);
    }

    @Override
    protected float getAnimationProgress(CockatriceEntity cockatrice, float f) {
        float f1 = cockatrice.prevWingRotation + (cockatrice.wingRotation - cockatrice.prevWingRotation) * f;
        float f2 = cockatrice.prevDestPos + (cockatrice.destPos - cockatrice.prevDestPos) * f;

        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    @Override
    public Identifier getTexture(CockatriceEntity cockatrice) {
        return TEXTURE;
    }
}
