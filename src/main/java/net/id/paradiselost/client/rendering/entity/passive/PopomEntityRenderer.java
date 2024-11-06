package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PigEntityRenderer;
import net.id.paradiselost.client.rendering.entity.passive.PopomEntityRenderer;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PopomEntityRenderer extends PigEntityRenderer {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/popom/popom.png");

    public PopomEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager);
    }

    public Identifier getTexture(PigEntity entity) {
        return TEXTURE;
    }
}