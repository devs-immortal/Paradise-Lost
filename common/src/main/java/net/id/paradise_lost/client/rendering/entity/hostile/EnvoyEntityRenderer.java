package net.id.paradise_lost.client.rendering.entity.hostile;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.EnvoyEyesFeatureRenderer;
import net.id.paradise_lost.entity.hostile.EnvoyEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.resources.ResourceLocation;

public class EnvoyEntityRenderer extends SkeletonRenderer<EnvoyEntity> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/envoy/envoy.png");
    private static final ResourceLocation TEXTURE_ENLIGHTENED = ModConstants.id("textures/entity/envoy/envoy_enlightened.png");

    public EnvoyEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, ParadiseLostModelLayers.ENVOY, ParadiseLostModelLayers.ENVOY_INNER_ARMOR, ParadiseLostModelLayers.ENVOY_OUTER_ARMOR);
        this.addLayer(new EnvoyEyesFeatureRenderer(this));
    }

    public ResourceLocation getTextureLocation(EnvoyEntity entity) {
        if (entity.getEnlightened()) {
            return TEXTURE_ENLIGHTENED;
        }
        return TEXTURE;
    }
}
