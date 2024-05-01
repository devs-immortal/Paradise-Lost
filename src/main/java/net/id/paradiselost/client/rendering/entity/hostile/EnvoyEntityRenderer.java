package net.id.paradiselost.client.rendering.entity.hostile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.EnvoyEyesFeatureRenderer;
import net.id.paradiselost.entities.hostile.EnvoyEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.SkeletonEntityRenderer;
import net.minecraft.entity.mob.AbstractSkeletonEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class EnvoyEntityRenderer extends SkeletonEntityRenderer {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/envoy/envoy.png");
    private static final Identifier TEXTURE_ENLIGHTENED = ParadiseLost.locate("textures/entity/envoy/envoy_enlightened.png");

    public EnvoyEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, ParadiseLostModelLayers.ENVOY, ParadiseLostModelLayers.ENVOY_INNER_ARMOR, ParadiseLostModelLayers.ENVOY_OUTER_ARMOR);
        this.addFeature(new EnvoyEyesFeatureRenderer(this));
    }

    public Identifier getTexture(AbstractSkeletonEntity entity) {
        if (((EnvoyEntity) entity).getEnlightened()) {
            return TEXTURE_ENLIGHTENED;
        }
        return TEXTURE;
    }
}
