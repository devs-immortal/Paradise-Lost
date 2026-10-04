package net.id.paradise_lost.client.rendering.entity.hostile;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.EnvoyEntityModel;
import net.id.paradise_lost.client.model.entity.EnvoyEyesFeatureRenderer;
import net.id.paradise_lost.client.rendering.entity.state.EnvoyEntityRenderState;
import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.minecraft.client.renderer.entity.AbstractSkeletonRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EnvoyEntityRenderer extends AbstractSkeletonRenderer<EnvoyEntity, EnvoyEntityRenderState> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/envoy/envoy.png");
    private static final ResourceLocation TEXTURE_ENLIGHTENED = ModConstants.id("textures/entity/envoy/envoy_enlightened.png");

    public EnvoyEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, ParadiseLostModelLayers.ENVOY_INNER_ARMOR, ParadiseLostModelLayers.ENVOY_OUTER_ARMOR, new EnvoyEntityModel(renderManager.bakeLayer(ParadiseLostModelLayers.ENVOY)));
        this.addLayer(new EnvoyEyesFeatureRenderer(this));
    }

    @Override
    public EnvoyEntityRenderState createRenderState() {
        return new EnvoyEntityRenderState();
    }

    @Override
    public void extractRenderState(EnvoyEntity envoy, EnvoyEntityRenderState state, float partialTick) {
        super.extractRenderState(envoy, state, partialTick);
        state.enlightened = envoy.getEnlightened();
    }

    @Override
    public ResourceLocation getTextureLocation(EnvoyEntityRenderState state) {
        if (state.enlightened) {
            return TEXTURE_ENLIGHTENED;
        }
        return TEXTURE;
    }
}
