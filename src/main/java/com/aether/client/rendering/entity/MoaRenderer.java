package com.aether.client.rendering.entity;

import com.aether.client.model.entity.MoaModel;
import com.aether.entities.passive.MoaEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class MoaRenderer extends MobEntityRenderer<MoaEntity, MoaModel> {

    public MoaRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new MoaModel(0.0F), 1.0F);

        // TODO: Fix code for Moa Rendering
        //this.addFeature(new MoaCustomizerLayer(renderManager, (MoaModel) this.getModel()));
        //this.addFeature(new MoaSaddleLayer(this));
        //this.addFeature(new SaddleFeatureRenderer<>(this, new MoaModel(0.5F), /* TODO */));
    }

    @Override
    protected float getAnimationProgress(MoaEntity moa, float partialTicks) {
        float f1 = moa.prevWingRotation + (moa.wingRotation - moa.prevWingRotation) * partialTicks;
        float f2 = moa.prevDestPos + (moa.destPos - moa.prevDestPos) * partialTicks;

        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    @Override
    protected void scale(MoaEntity moa, MatrixStack matrixStack, float partialTicks) {
        float moaScale = moa.isBaby() ? 1.0F : 1.8F;

        matrixStack.scale(moaScale, moaScale, moaScale);
    }

    @Override
    public Identifier getTexture(MoaEntity entity) {

        if (entity.hasPassengers() && entity.getPassengerList().get(0) instanceof PlayerEntity) {
//            IPlayerAether player = AetherAPI.get((PlayerEntity) entity.getPassengerList().get(0));

//            if (player instanceof PlayerAether && !((PlayerAether) player).donationPerks.getMoaSkin().shouldUseDefualt())
//                return null;
        }
        return entity.getMoaType().getTexture(false);
    }
}
