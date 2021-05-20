package com.aether.client.rendering.entity;

import com.aether.Aether;
import com.aether.client.model.entity.MoaModel;
import com.aether.entities.passive.MoaEntity;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class MoaRenderer extends MobEntityRenderer<MoaEntity, MoaModel> {

    public MoaRenderer(EntityRenderDispatcher renderManager) {
        super(renderManager, new MoaModel(), 1f);
    }

    @Override
    protected void scale(MoaEntity moa, MatrixStack matrixStack, float partialTicks) {
        float moaScale = moa.isBaby() ? 0.3334F : 1.0F;
        matrixStack.scale(moaScale, moaScale, moaScale);
    }

    @Override
    public Identifier getTexture(MoaEntity entity) {

        if (entity.hasPassengers() && entity.getPassengerList().get(0) instanceof PlayerEntity) {
//            IPlayerAether player = AetherAPI.get((PlayerEntity) entity.getPassengerList().get(0));

//            if (player instanceof PlayerAether && !((PlayerAether) player).donationPerks.getMoaSkin().shouldUseDefualt())
//                return null;
        }

        return Aether.locate("textures/entity/moas/highlands/blue.png");
    }
}
