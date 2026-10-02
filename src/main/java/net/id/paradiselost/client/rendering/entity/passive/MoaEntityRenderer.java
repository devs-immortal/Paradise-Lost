package net.id.paradiselost.client.rendering.entity.passive;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.id.paradiselost.client.model.entity.MoaModel;
import net.id.paradiselost.client.rendering.entity.state.MoaEntityRenderState;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class MoaEntityRenderer extends MobEntityRenderer<MoaEntity, MoaEntityRenderState, MoaModel> {

    public MoaEntityRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new MoaModel(renderManager.getPart(ParadiseLostModelLayers.MOA)), 0.7f);
    }

    @Override
    protected void scale(MoaEntityRenderState state, MatrixStack matrixStack) {
        matrixStack.scale(state.moaScale, state.moaScale, state.moaScale);
    }

    @Override
    protected int getBlockLight(MoaEntity moa, BlockPos pos) {
        return moa.getGenes().getRace().glowing() ? 15 : super.getBlockLight(moa, pos);
    }

    @Override
    public MoaEntityRenderState createRenderState() {
        return new MoaEntityRenderState();
    }

    @Override
    public void updateRenderState(MoaEntity moa, MoaEntityRenderState state, float tickDelta) {
        super.updateRenderState(moa, state, tickDelta);
        state.saddled = moa.isSaddled();
        state.hasChest = moa.hasChest();
        state.inAir = moa.isInAir;
        state.legPitch = moa.getLegPitch();
        state.wingRoll = moa.getWingRoll();
        state.wingYaw = moa.getWingYaw();
        state.horizontalSpeed = (float) new Vec3d(moa.getVelocity().getX(), 0, moa.getVelocity().getZ()).length();
        state.glowing = moa.getGenes().getRace().glowing();
        state.texture = moa.getGenes().getTexture();
        state.moaScale = moa.isBaby() ? Math.min(0.43F + (moa.age * 0.00001f), 0.72f) : 1.0F;
    }

    @Override
    public Identifier getTexture(MoaEntityRenderState state) {
        return state.texture;
    }
}
