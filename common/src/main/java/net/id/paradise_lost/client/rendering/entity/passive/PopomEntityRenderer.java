package net.id.paradise_lost.client.rendering.entity.passive;

import net.id.paradise_lost.client.rendering.entity.state.PopomEntityRenderState;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.PopomEntityModel;
import net.id.paradise_lost.entity.passive.PopomEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class PopomEntityRenderer extends MobRenderer<PopomEntity, PopomEntityRenderState, PopomEntityModel> {
    private static final ResourceLocation TEXTURE = ModConstants.id("textures/entity/popom/popom.png");

    public PopomEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new PopomEntityModel(renderManager.bakeLayer(ParadiseLostModelLayers.POPOM)), 0.7F);
    }

    @Override
    public PopomEntityRenderState createRenderState() {
        return new PopomEntityRenderState();
    }

    @Override
    public void extractRenderState(PopomEntity popom, PopomEntityRenderState state, float partialTick) {
        super.extractRenderState(popom, state, partialTick);
        state.furSize = popom.getFurSize();
        state.headAngle = popom.getHeadAngle(state.ageInTicks);
    }

    @Override
    public ResourceLocation getTextureLocation(PopomEntityRenderState state) {
        return TEXTURE;
    }
}
