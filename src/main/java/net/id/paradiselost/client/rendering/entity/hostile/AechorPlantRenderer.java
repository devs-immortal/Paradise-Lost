package net.id.paradiselost.client.rendering.entity.hostile;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.AechorPlantModel;
import net.id.paradiselost.entities.hostile.AechorPlantEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AechorPlantRenderer extends MobEntityRenderer<AechorPlantEntity, AechorPlantModel> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/aechor_plant.png");

    public AechorPlantRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AechorPlantModel(renderManager.getPart(ParadiseLostModelLayers.AECHOR_PLANT)), 0.3F);
    }

    @Override
    public Identifier getTexture(AechorPlantEntity entity) {
        return TEXTURE;
    }
}
