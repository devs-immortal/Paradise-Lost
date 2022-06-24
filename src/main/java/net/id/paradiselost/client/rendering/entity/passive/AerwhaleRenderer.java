package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.AerwhaleModel;
import net.id.paradiselost.entities.passive.AerwhaleEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AerwhaleRenderer extends MobEntityRenderer<AerwhaleEntity, AerwhaleModel> {

    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/aerwhale/aerwhale.png");

    public AerwhaleRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AerwhaleModel(renderManager.getPart(ParadiseLostModelLayers.AERWHALE)), 0.3F);
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
