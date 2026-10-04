package net.id.paradise_lost.client.rendering.entity.hostile;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.QuintEntityModel;
import net.id.paradise_lost.entity.passive.QuintEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;

public class QuintEntityRenderer extends MobRenderer<QuintEntity, LivingEntityRenderState, QuintEntityModel> {

    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/quint.png");

    public QuintEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new QuintEntityModel(renderManager.bakeLayer(ParadiseLostModelLayers.QUINT)), 0.0F);
    }

    @Override
    protected int getBlockLightLevel(QuintEntity quintEntity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState state) {
        return TEXTURE;
    }
}
