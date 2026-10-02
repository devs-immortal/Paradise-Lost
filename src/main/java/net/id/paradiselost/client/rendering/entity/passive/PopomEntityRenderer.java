package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.PopomEntityModel;
import net.id.paradiselost.client.rendering.entity.state.PopomEntityRenderState;
import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class PopomEntityRenderer extends MobEntityRenderer<PopomEntity, PopomEntityRenderState, PopomEntityModel> {
    private static final Identifier TEXTURE = ParadiseLost.locate("textures/entity/popom/popom.png");

    public PopomEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new PopomEntityModel(renderManager.getPart(ParadiseLostModelLayers.POPOM)), 0.7F);
    }

    @Override
    public PopomEntityRenderState createRenderState() {
        return new PopomEntityRenderState();
    }

    @Override
    public void updateRenderState(PopomEntity popom, PopomEntityRenderState state, float tickDelta) {
        super.updateRenderState(popom, state, tickDelta);
        state.furSize = popom.getFurSize();
        state.headAngle = popom.getHeadAngle(state.age);
    }

    @Override
    public Identifier getTexture(PopomEntityRenderState state) {
        return TEXTURE;
    }
}
