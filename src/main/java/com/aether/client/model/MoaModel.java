package com.aether.client.model;

import com.aether.entities.passive.MoaEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Function;

public class MoaModel extends BipedEntityModel<MoaEntity> {

    public ModelPart head, body;
    public ModelPart legs, legs2;
    public ModelPart wings, wings2;
    public ModelPart jaw, neck;
    public ModelPart feather1, feather2, feather3;
    private MoaEntity entityIn;

    public MoaModel(float scale) {
        this(RenderLayer::getEntityCutoutNoCull, scale, 0.0F, 64, 32);
    }

    public MoaModel(Function<Identifier, RenderLayer> texturedLayerFactory, float scale, float f, int textureWidth, int textureHeight) {
        super(texturedLayerFactory, scale, f, textureWidth, textureHeight);
        this.head = new ModelPart(this, 0, 13);
        this.head.addCuboid(-2.0F, -4.0F, -6.0F, 4, 4, 8, 0.0F);
        this.head.setPivot(0.0F, (float) (-8 + 16), -4.0F);

        this.jaw = new ModelPart(this, 24, 13);
        this.jaw.addCuboid(-2.0F, -1.0F, -6.0F, 4, 1, 8, -0.1F);
        this.jaw.setPivot(0.0F, (float) (-8 + 16), -4.0F);

        this.body = new ModelPart(this, 0, 0);
        this.body.addCuboid(-3.0F, -3.0F, 0.0F, 6, 8, 5, scale);
        this.body.setPivot(0.0F, (float) (16), 0.0F);

        this.legs = new ModelPart(this, 22, 0);
        this.legs.addCuboid(-1.0F, -1.0F, -1.0F, 2, 9, 2, 0.0F);
        this.legs.setPivot(-2.0F, (float) (16), 1.0F);

        this.legs2 = new ModelPart(this, 22, 0);
        this.legs2.addCuboid(-1.0F, -1.0F, -1.0F, 2, 9, 2, 0.0F);
        this.legs2.setPivot(2.0F, (float) (16), 1.0F);

        this.wings = new ModelPart(this, 52, 0);
        this.wings.addCuboid(-1.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings.setPivot(-3.0F, (float) (16), 2.0F);

        this.wings2 = new ModelPart(this, 52, 0);
        this.wings2.addCuboid(0.0F, -0.0F, -1.0F, 1, 8, 4);
        this.wings2.setPivot(3.0F, (float) (-4 + 16), 0.0F);

        this.neck = new ModelPart(this, 44, 0);
        this.neck.addCuboid(-1.0F, -6.0F, -1.0F, 2, 6, 2);
        this.neck.setPivot(0.0F, (float) (-2 + 16), -4.0F);

        this.feather1 = new ModelPart(this, 30, 0);
        this.feather1.addCuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather1.setPivot(0.0F, (float) (1 + 16), 1.0F);
        this.feather2 = new ModelPart(this, 30, 0);
        this.feather2.addCuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather2.setPivot(0.0F, (float) (1 + 16), 1.0F);
        this.feather3 = new ModelPart(this, 30, 0);
        this.feather3.addCuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5, -0.3F);
        this.feather3.setPivot(0.0F, (float) (1 + 16), 1.0F);
        this.feather1.pivotY += 0.5F;
        this.feather2.pivotY += 0.5F;
        this.feather3.pivotY += 0.5F;
    }

    @Override
    public void animateModel(MoaEntity entity, float f, float g, float h) {
        entityIn = entity;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        if (!entityIn.isSitting() || (!entityIn.isOnGround() && entityIn.isSitting())) {
            this.legs.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.legs2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }

        this.head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.jaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.wings.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.wings2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.neck.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(MoaEntity moaIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.entityIn = moaIn;
        this.head.pitch = headPitch / 57.29578F;
        this.head.yaw = netHeadYaw / 57.29578F;

        this.jaw.pitch = head.pitch;
        this.jaw.yaw = head.yaw;

        this.body.pitch = 1.570796F;

        this.legs.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legs2.pitch = MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;

        if (ageInTicks > 0.001F) {
            this.wings.pivotZ = -1F;
            this.wings2.pivotZ = -1F;
            this.wings.pivotY = 12F;
            this.wings2.pivotY = 12F;
            this.wings.pitch = 0.0F;
            this.wings2.pitch = 0.0F;
            this.wings.roll = ageInTicks;
            this.wings2.roll = -ageInTicks;
            this.legs.pitch = 0.6F;
            this.legs2.pitch = 0.6F;
        } else {
            this.wings.pivotZ = -3F;
            this.wings2.pivotZ = -3F;
            this.wings.pivotY = 14F;
            this.wings2.pivotY = 14F;
            this.wings.pitch = (float) (Math.PI / 2.0F);
            this.wings2.pitch = (float) (Math.PI / 2.0F);
            this.wings.roll = 0.0F;
            this.wings2.roll = 0.0F;
        }

        if (moaIn.isSitting()) {
            this.head.setPivot(0.0F, (float) (-8 + 24), -4.0F);
            this.jaw.setPivot(0.0F, (float) (-8 + 24), -4.0F);
            this.body.setPivot(0.0F, (float) (24), 0.0F);
            this.legs.setPivot(-2.0F, (float) (24), 1.0F);
            this.legs2.setPivot(2.0F, (float) (24), 1.0F);
            this.neck.setPivot(0.0F, (float) (-2 + 24), -4.0F);
            this.feather1.setPivot(0.0F, (float) (1 + 24), 1.0F);
            this.feather2.setPivot(0.0F, (float) (1 + 24), 1.0F);
            this.feather3.setPivot(0.0F, (float) (1 + 24), 1.0F);

            this.jaw.pitch = -0.3F;
            this.head.pitch = 0F;

            this.wings.pivotY = 22F;
            this.wings2.pivotY = 22F;
        } else {
            this.head.setPivot(0.0F, (float) (-8 + 16), -4.0F);
            this.jaw.setPivot(0.0F, (float) (-8 + 16), -4.0F);
            this.body.setPivot(0.0F, (float) (16), 0.0F);
            this.legs.setPivot(-2.0F, (float) (16), 1.0F);
            this.legs2.setPivot(2.0F, (float) (16), 1.0F);
            this.neck.setPivot(0.0F, (float) (-2 + 16), -4.0F);
            this.feather1.setPivot(0.0F, (float) (1 + 16), 1.0F);
            this.feather2.setPivot(0.0F, (float) (1 + 16), 1.0F);
            this.feather3.setPivot(0.0F, (float) (1 + 16), 1.0F);
        }

        this.feather1.yaw = -0.375F;
        this.feather2.yaw = 0.0F;
        this.feather3.yaw = 0.375F;
        this.feather1.pitch = 0.25F;
        this.feather2.pitch = 0.25F;
        this.feather3.pitch = 0.25F;

        this.neck.pitch = 0.0F;
        this.neck.yaw = head.yaw;
        this.jaw.pitch += 0.35F;
    }
}
