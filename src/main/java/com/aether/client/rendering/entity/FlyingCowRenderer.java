package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.FlyingCowModel;
import com.aether.client.rendering.entity.layer.FlyingCowWingLayer;
import com.aether.entities.passive.FlyingCowEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.SaddleFeatureRenderer;
import net.minecraft.util.Identifier;

public class FlyingCowRenderer extends MobEntityRenderer<FlyingCowEntity, FlyingCowModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/flying_cow/flying_cow.png");

    public FlyingCowRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new FlyingCowModel(0.0F), 0.7F);

        this.addFeature(new FlyingCowWingLayer(this));
        this.addFeature(new SaddleFeatureRenderer<>(this, new FlyingCowModel(0.5F), Aether.locate("textures/entity/flying_cow/saddle.png")));
    }

    @Override
    public Identifier getTexture(FlyingCowEntity entity) {
        return TEXTURE;
    }
}
