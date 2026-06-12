package net.id.paradiselost.client.rendering.entity.hostile;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.SentinelEntityModel;
import net.id.paradiselost.client.model.entity.SentinelEyesFeatureRenderer;
import net.id.paradiselost.client.rendering.entity.state.SentinelEntityRenderState;
import net.id.paradiselost.entities.hostile.SentinelEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class SentinelEntityRenderer extends BipedEntityRenderer<SentinelEntity, SentinelEntityRenderState, SentinelEntityModel<SentinelEntityRenderState>> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/sentinel/sentinel.png");

    public SentinelEntityRenderer(EntityRendererFactory.Context renderManager) {
        this(renderManager, ParadiseLostModelLayers.SENTINEL, ParadiseLostModelLayers.SENTINEL_INNER_ARMOR, ParadiseLostModelLayers.SENTINEL_OUTER_ARMOR);
        this.addFeature(new SentinelEyesFeatureRenderer(this));
    }

    @Override
    public Identifier getTexture(SentinelEntityRenderState state) {
        return TEXTURE;
    }

    public SentinelEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new SentinelEntityModel<>(ctx.getPart(layer)), 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new SentinelEntityModel<>(ctx.getPart(legsArmorLayer)), new SentinelEntityModel<>(ctx.getPart(bodyArmorLayer)), ctx.getEquipmentRenderer()));
    }

    @Override
    public SentinelEntityRenderState createRenderState() {
        return new SentinelEntityRenderState();
    }

    @Override
    public void updateRenderState(SentinelEntity sentinel, SentinelEntityRenderState state, float tickDelta) {
        super.updateRenderState(sentinel, state, tickDelta);
        state.enlightened = sentinel.getEnlightened();
    }

    @Override
    protected float getShadowRadius(SentinelEntityRenderState state) {
        return state.enlightened ? super.getShadowRadius(state) : 0F;
    }
}
