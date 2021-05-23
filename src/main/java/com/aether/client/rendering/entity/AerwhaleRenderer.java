package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.AerwhaleModel;
import com.aether.entities.passive.AerwhaleEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

public class AerwhaleRenderer  extends MobEntityRenderer<AerwhaleEntity, AerwhaleModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/aerwhale/aerwhale.png");

    public AerwhaleRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new AerwhaleModel(), 0.3F);
    }

    @Override
    public AerwhaleModel getModel() {
        return super.getModel();
    }

    @Override
    public Identifier getTexture(AerwhaleEntity entity) {
        return TEXTURE;
    }

}
