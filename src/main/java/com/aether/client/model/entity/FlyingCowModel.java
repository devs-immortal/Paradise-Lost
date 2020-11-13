package com.aether.client.model.entity;

import com.aether.entities.passive.FlyingCowEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class FlyingCowModel extends QuadrupedEntityModel<FlyingCowEntity> {

    private final ModelPart udders;
    private final ModelPart horn1;
    private final ModelPart horn2;

    public FlyingCowModel(float scale) {
        this(12, scale, false, 10.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public FlyingCowModel(int legHeight, float scale, boolean headScaled, float childHeadYOffset, float childHeadZOffset, float invertedChildHeadScale, float invertedChildBodyScale, int childBodyYOffset) {
        super(legHeight, scale, headScaled, childHeadYOffset, childHeadZOffset, invertedChildHeadScale, invertedChildBodyScale, childBodyYOffset);
        this.head = new ModelPart(this, 0, 0);
        this.head.addCuboid(-4.0F, -4.0F, -6.0F, 8, 8, 6, 0.0F);
        this.head.setPivot(0.0F, 4.0F, -8.0F);
        this.horn1 = new ModelPart(this, 22, 0);
        this.horn1.addCuboid(-4.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.horn1.setPivot(0.0F, 3.0F, -7.0F);
        this.horn2 = new ModelPart(this, 22, 0);
        this.horn2.addCuboid(3.0F, -5.0F, -4.0F, 1, 3, 1, 0.0F);
        this.horn2.setPivot(0.0F, 3.0F, -7.0F);
        this.udders = new ModelPart(this, 52, 0);
        this.udders.addCuboid(-2.0F, -3.0F, 0.0F, 4, 6, 2, 0.0F);
        this.udders.setPivot(0.0F, 14.0F, 6.0F);
        this.udders.pitch = ((float) Math.PI / 2F);
        this.torso = new ModelPart(this, 18, 4);
        this.torso.addCuboid(-6.0F, -10.0F, -7.0F, 12, 18, 10, scale);
        this.torso.setPivot(0.0F, 5.0F, 2.0F);

        --this.backRightLeg.pivotX;
        ++this.backLeftLeg.pivotX;
        this.backRightLeg.pivotZ += 0.0F;
        this.backLeftLeg.pivotZ += 0.0F;
        --this.frontRightLeg.pivotX;
        ++this.frontLeftLeg.pivotX;
        --this.frontRightLeg.pivotZ;
        --this.frontLeftLeg.pivotZ;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        super.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

        if (this.child) {
            float f6 = 2.0F;
            matrices.push();
            matrices.pop();
            matrices.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
            matrices.translate(0.0F, 24.0F/* * scale*/, 0.0F);
            this.horn1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.horn2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.udders.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        } else {
            this.horn1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.horn2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.udders.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void setAngles(FlyingCowEntity entityIn, float limbSwing, float limbSwingAmount, float customAngle, float netHeadYaw, float headPitch) {
        super.setAngles(entityIn, limbSwing, limbSwingAmount, customAngle, netHeadYaw, headPitch);

        this.horn1.yaw = this.head.yaw;
        this.horn1.pitch = this.head.pitch;
        this.horn2.yaw = this.head.yaw;
        this.horn2.pitch = this.head.pitch;
    }

}
