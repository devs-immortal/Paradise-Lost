package net.id.paradiselost.client.rendering.entity.hostile;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.QuintEntityModel;
import net.id.paradiselost.entities.hostile.QuintEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class QuintEntityRenderer extends MobEntityRenderer<QuintEntity, QuintEntityModel> {

    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/quint.png");

    public QuintEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new QuintEntityModel(renderManager.getPart(ParadiseLostModelLayers.QUINT)), 0.0F);
    }

    protected int getBlockLight(QuintEntity quintEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTexture(QuintEntity entity) {
        return TEXTURE;
    }
}
