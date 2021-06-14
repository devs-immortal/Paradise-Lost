package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.entities.passive.PhygEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class PhygRenderer extends MobEntityRenderer<PhygEntity, PigEntityModel<PhygEntity>> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/phyg/phyg.png");

    public PhygRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PigEntityModel<>(renderManager.getPart(EntityModelLayers.PIG)), 0.7F);

        //this.addFeature(new PhygWingLayer(this));
        this.addFeature(new SaddleFeatureRenderer<>(this, new PigEntityModel<>(renderManager.getPart(EntityModelLayers.PIG_SADDLE)), new Identifier("textures/entity/pig/pig_saddle.png")));
    }

    @Override
    public Identifier getTexture(PhygEntity entity) {
        return TEXTURE;
    }
}
