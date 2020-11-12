package com.aether.client.model;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import java.util.function.Function;

public class PlayerWingModel extends BipedEntityModel<AbstractClientPlayerEntity> {

    public ModelPart wingLeft;
    public ModelPart wingRight;
    public float sinage;
    public boolean gonRound;

    public PlayerWingModel(float scale) {
        this(RenderLayer::getEntityCutoutNoCull, scale, 0.0F, 64, 32);
    }

    public PlayerWingModel(Function<Identifier, RenderLayer> texturedLayerFactory, float scale, float f, int textureWidth, int textureHeight) {
        super(texturedLayerFactory, scale, f, textureWidth, textureHeight);
        this.leftArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.rightArmPose = BipedEntityModel.ArmPose.EMPTY;
        this.sneaking = false;

        this.wingLeft = new ModelPart(this, 24, 31);
        this.wingLeft.addCuboid(0F, -7F, 1F, 20, 8, 0, f);
        this.wingLeft.setPivot(0.5F, 5F + f, 2.625F);

        this.wingRight = new ModelPart(this, 24, 31);
        this.wingRight.mirror = true;
        this.wingRight.addCuboid(-19F, -7F, 1F, 20, 8, 0, f);
        this.wingRight.setPivot(-0.5F, 5F + f, 2.625F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.wingLeft.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.wingRight.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(AbstractClientPlayerEntity player, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setAngles(player, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        this.gonRound = player.isOnGround();

        this.wingLeft.yaw = -0.4F;
        this.wingRight.yaw = 0.4F;
        this.wingLeft.roll = -0.125F;
        this.wingRight.roll = 0.125F;

        if (player.isSneaking()) {
            this.wingLeft.pitch = 0.45F;
            this.wingRight.pitch = 0.45F;
            this.wingLeft.pivotY = -0.17F;
            this.wingRight.pivotY = -0.17F;
            this.wingLeft.pivotZ = 0.112F;
            this.wingRight.pivotZ = 0.112F;
        } else {
            this.wingLeft.pitch = this.wingLeft.pivotZ = this.wingLeft.pivotY =
                    this.wingRight.pitch = this.wingRight.pivotZ = this.wingRight.pivotY = 0.0F;
        }

        this.wingLeft.yaw += (float) Math.sin(this.sinage) / 6F;
        this.wingRight.yaw -= Math.sin(this.sinage) / 6F;
        this.wingLeft.roll += (float) Math.cos(this.sinage) / (this.gonRound ? 8F : 3F);
        this.wingRight.roll -= Math.cos(this.sinage) / (this.gonRound ? 8F : 3F);
    }

}
