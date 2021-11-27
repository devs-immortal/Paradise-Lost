package net.id.aether.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.client.model.AetherModelLayers;
import net.id.aether.client.model.entity.AerwhaleModel;
import net.id.aether.entities.passive.AerwhaleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AerwhaleRenderer extends MobEntityRenderer<AerwhaleEntity, AerwhaleModel> {

    private static final Identifier TEXTURE = Aether.locate("textures/entity/aerwhale/aerwhale.png");

    public AerwhaleRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AerwhaleModel(renderManager.getPart(AetherModelLayers.AERWHALE)), 0.3F);
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
