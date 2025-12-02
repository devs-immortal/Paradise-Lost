package net.id.paradiselost.client.rendering.entity.hostile;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.KeeperEntityModel;
import net.id.paradiselost.entities.hostile.KeeperEntity;
import net.minecraft.client.render.entity.BipedEntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

public class KeeperEntityRenderer extends BipedEntityRenderer<KeeperEntity, KeeperEntityModel<KeeperEntity>> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/keeper/keeper.png");
    private static final Identifier TEXTURE_ENLIGHTENED = ParadiseLost.locate("textures/entity/keeper/keeper_enlightened.png");

    public KeeperEntityRenderer(EntityRendererFactory.Context renderManager) {
        this(renderManager, ParadiseLostModelLayers.KEEPER, ParadiseLostModelLayers.KEEPER_INNER_ARMOR, ParadiseLostModelLayers.KEEPER_OUTER_ARMOR);
        //this.addFeature(new EnvoyEyesFeatureRenderer(this)); TODO
    }

    public Identifier getTexture(KeeperEntity entity) {
        if (entity.getEnlightened()) {
            return TEXTURE_ENLIGHTENED;
        }
        return TEXTURE;
    }

    public KeeperEntityRenderer(EntityRendererFactory.Context ctx, EntityModelLayer layer, EntityModelLayer legsArmorLayer, EntityModelLayer bodyArmorLayer) {
        super(ctx, new KeeperEntityModel<>(ctx.getPart(layer)), 0.5F);
        this.addFeature(new ArmorFeatureRenderer<>(this, new KeeperEntityModel<>(ctx.getPart(legsArmorLayer)), new KeeperEntityModel<>(ctx.getPart(bodyArmorLayer)), ctx.getModelManager()));
    }

    protected float getShadowRadius(KeeperEntity keeperEntity) {
        return keeperEntity.getEnlightened() ? super.getShadowRadius(keeperEntity) * keeperEntity.getScaleFactor() : 0F;
    }
}
