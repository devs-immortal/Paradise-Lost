package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.AmbystModel;
import net.id.paradiselost.entities.passive.ambyst.AmbystEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class AmbystRenderer extends MobEntityRenderer<AmbystEntity, AmbystModel> {

    public AmbystRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AmbystModel(renderManager.getPart(ParadiseLostModelLayers.AMBYST)), 0.5f);
    }

    @Override
    public Identifier getTexture(AmbystEntity entity) {
        return new Identifier("textures/entity/axolotl/axolotl_blue.png");
    }
}