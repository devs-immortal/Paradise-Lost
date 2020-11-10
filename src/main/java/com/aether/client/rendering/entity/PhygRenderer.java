package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.rendering.entity.layer.PhygSaddleLayer;
import com.aether.client.rendering.entity.layer.PhygWingLayer;
import com.aether.entities.passive.PhygEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.PigEntityModel;
import net.minecraft.util.Identifier;

public class PhygRenderer extends MobEntityRenderer<PhygEntity, PigEntityModel<PhygEntity>> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/phyg/phyg.png");

    public PhygRenderer(EntityRenderDispatcher rendermanagerIn) {
        super(rendermanagerIn, new PigEntityModel<>(), 0.7F);

        this.addFeature(new PhygWingLayer(this));
        this.addFeature(new PhygSaddleLayer(this));
    }

    @Override
    public Identifier getTexture(PhygEntity entity) {
        return TEXTURE;
    }
}
