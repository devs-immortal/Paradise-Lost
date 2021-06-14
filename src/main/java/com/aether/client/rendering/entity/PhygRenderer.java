package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.entities.passive.PhygEntity;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SaddleLayer;
import net.minecraft.resources.ResourceLocation;

public class PhygRenderer extends MobRenderer<PhygEntity, PigModel<PhygEntity>> {

    private static final ResourceLocation TEXTURE = Aether.locate("textures/entity/phyg/phyg.png");

    public PhygRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PigModel<>(renderManager.bakeLayer(ModelLayers.PIG)), 0.7F);

        //this.addFeature(new PhygWingLayer(this));
        this.addLayer(new SaddleLayer<>(this, new PigModel<>(renderManager.bakeLayer(ModelLayers.PIG_SADDLE)), new ResourceLocation("textures/entity/pig/pig_saddle.png")));
    }

    @Override
    public ResourceLocation getTextureLocation(PhygEntity entity) {
        return TEXTURE;
    }
}
