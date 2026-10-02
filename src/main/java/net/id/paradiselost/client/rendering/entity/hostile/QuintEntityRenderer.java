package net.id.paradiselost.client.rendering.entity.hostile;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.QuintEntityModel;
import net.id.paradiselost.entities.passive.QuintEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class QuintEntityRenderer extends MobEntityRenderer<QuintEntity, LivingEntityRenderState, QuintEntityModel> {

    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/quint.png");

    public QuintEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new QuintEntityModel(renderManager.getPart(ParadiseLostModelLayers.QUINT)), 0.0F);
    }

    @Override
    protected int getBlockLight(QuintEntity quintEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public Identifier getTexture(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
