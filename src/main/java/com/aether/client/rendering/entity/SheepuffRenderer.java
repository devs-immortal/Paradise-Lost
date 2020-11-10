package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.SheepuffWoolModel;
import com.aether.client.rendering.entity.layer.SheepuffCoatLayer;
import com.aether.entities.passive.SheepuffEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class SheepuffRenderer extends MobEntityRenderer<SheepuffEntity, SheepuffWoolModel> {

    public SheepuffRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new SheepuffWoolModel(0.0f), 0.7F);

        this.addFeature(new SheepuffCoatLayer(this));
    }

    @Override
    public Identifier getTexture(SheepuffEntity entity) {
        return Aether.locate("textures/entity/sheepuff/sheepuff.png");
    }
}
