package net.id.paradiselost.client.rendering.entity.hostile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.EnvoyEntityModel;
import net.id.paradiselost.client.model.entity.EnvoyEyesFeatureRenderer;
import net.id.paradiselost.client.rendering.entity.state.EnvoyEntityRenderState;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.minecraft.client.render.entity.AbstractSkeletonEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnvoyEntityRenderer extends AbstractSkeletonEntityRenderer<EnvoyEntity, EnvoyEntityRenderState> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/envoy/envoy.png");
    private static final Identifier TEXTURE_ENLIGHTENED = ParadiseLost.locate("textures/entity/envoy/envoy_enlightened.png");

    public EnvoyEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, ParadiseLostModelLayers.ENVOY_INNER_ARMOR, ParadiseLostModelLayers.ENVOY_OUTER_ARMOR, new EnvoyEntityModel(renderManager.getPart(ParadiseLostModelLayers.ENVOY)));
        this.addFeature(new EnvoyEyesFeatureRenderer(this));
    }

    @Override
    public EnvoyEntityRenderState createRenderState() {
        return new EnvoyEntityRenderState();
    }

    @Override
    public void updateRenderState(EnvoyEntity envoy, EnvoyEntityRenderState state, float tickDelta) {
        super.updateRenderState(envoy, state, tickDelta);
        state.enlightened = envoy.getEnlightened();
    }

    @Override
    public Identifier getTexture(EnvoyEntityRenderState state) {
        if (state.enlightened) {
            return TEXTURE_ENLIGHTENED;
        }
        return TEXTURE;
    }
}
