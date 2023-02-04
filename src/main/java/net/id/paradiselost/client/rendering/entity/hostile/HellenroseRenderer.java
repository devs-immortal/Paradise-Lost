package net.id.paradiselost.client.rendering.entity.hostile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.HellenroseModel;
import net.id.paradiselost.entities.hostile.HellenroseEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class HellenroseRenderer extends MobEntityRenderer<HellenroseEntity, HellenroseModel> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/hellenrose.png");

    public HellenroseRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HellenroseModel(renderManager.getPart(ParadiseLostModelLayers.HELLENROSE)), 0.3F);
    }

    @Override
    public Identifier getTexture(HellenroseEntity entity) {
        return TEXTURE;
    }
}
