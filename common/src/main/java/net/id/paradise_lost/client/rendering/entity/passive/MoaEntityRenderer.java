package net.id.paradise_lost.client.rendering.entity.passive;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.id.paradise_lost.client.model.entity.MoaModel;
import net.id.paradise_lost.client.rendering.entity.state.MoaEntityRenderState;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public class MoaEntityRenderer extends MobRenderer<MoaEntity, MoaEntityRenderState, MoaModel> {
    public MoaEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MoaModel(renderManager.bakeLayer(ParadiseLostModelLayers.MOA)), 0.7f);
    }

    @Override
    protected void scale(MoaEntityRenderState state, PoseStack matrixStack) {
        matrixStack.scale(state.moaScale, state.moaScale, state.moaScale);
    }

    @Override
    protected int getBlockLightLevel(MoaEntity moa, BlockPos pos) {
        return moa.getGenes().getRace().glowing() ? 15 : super.getBlockLightLevel(moa, pos);
    }

    @Override
    public MoaEntityRenderState createRenderState() {
        return new MoaEntityRenderState();
    }

    @Override
    public void extractRenderState(MoaEntity moa, MoaEntityRenderState state, float partialTick) {
        super.extractRenderState(moa, state, partialTick);
        state.saddled = moa.isSaddled();
        state.hasChest = moa.hasChest();
        state.inAir = moa.isInAir;
        state.legPitch = moa.getLegPitch();
        state.wingRoll = moa.getWingRoll();
        state.wingYaw = moa.getWingYaw();
        state.horizontalSpeed = (float) new Vec3(moa.getDeltaMovement().x(), 0, moa.getDeltaMovement().z()).length();
        state.texture = moa.getGenes().getTexture();
        state.moaScale = moa.isBaby() ? Math.min(0.43F + (moa.tickCount * 0.00001f), 0.72f) : 1.0F;
    }

    @Override
    public ResourceLocation getTextureLocation(MoaEntityRenderState state) {
        return state.texture;
    }
}
