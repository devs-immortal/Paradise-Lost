package net.id.paradise_lost.client.rendering.entity.hostile;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.SentinelEntityModel;
import net.id.paradise_lost.client.model.entity.SentinelEyesFeatureRenderer;
import net.id.paradise_lost.client.rendering.entity.state.SentinelEntityRenderState;
import net.id.paradise_lost.entity.hostile.SentinelEntity;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;

public class SentinelEntityRenderer extends HumanoidMobRenderer<SentinelEntity, SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/sentinel/sentinel.png");

    public SentinelEntityRenderer(EntityRendererProvider.Context renderManager) {
        this(renderManager, ParadiseLostModelLayers.SENTINEL, ParadiseLostModelLayers.SENTINEL_INNER_ARMOR, ParadiseLostModelLayers.SENTINEL_OUTER_ARMOR);
        this.addLayer(new SentinelEyesFeatureRenderer(this));
    }

    public SentinelEntityRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation layer, ModelLayerLocation legsArmorLayer, ModelLayerLocation bodyArmorLayer) {
        super(ctx, new SentinelEntityModel<>(ctx.bakeLayer(layer)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new SentinelEntityModel<>(ctx.bakeLayer(legsArmorLayer)), new SentinelEntityModel<>(ctx.bakeLayer(bodyArmorLayer)), ctx.getEquipmentRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(SentinelEntityRenderState state) {
        return TEXTURE;
    }

    @Override
    public SentinelEntityRenderState createRenderState() {
        return new SentinelEntityRenderState();
    }

    @Override
    public void extractRenderState(SentinelEntity sentinel, SentinelEntityRenderState state, float partialTick) {
        super.extractRenderState(sentinel, state, partialTick);
        state.enlightened = sentinel.getEnlightened();
    }

    @Override
    protected float getShadowRadius(SentinelEntityRenderState state) {
        return state.enlightened ? super.getShadowRadius(state) : 0F;
    }
}
