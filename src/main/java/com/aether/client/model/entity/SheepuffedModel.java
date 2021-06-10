package com.aether.client.model.entity;

import com.aether.entities.passive.SheepuffEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

public class SheepuffedModel extends QuadrupedEntityModel<SheepuffEntity> {

    private float headRotationAngleX;

    public SheepuffedModel(float scale) {
        this(12, scale, false, 10.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public SheepuffedModel(int legHeight, float scale, boolean headScaled, float childHeadYOffset, float childHeadZOffset, float invertedChildHeadScale, float invertedChildBodyScale, int childBodyYOffset) {
        super(legHeight, scale, headScaled, childHeadYOffset, childHeadZOffset, invertedChildHeadScale, invertedChildBodyScale, childBodyYOffset);

        this.head = new ModelPart(this, 0, 0);
        this.head.addCuboid(-3.0F, -4.0F, -4.0F, 6, 6, 6, 0.6F);
        this.head.setPivot(0.0F, 6.0F, -8.0F);

        this.body = new ModelPart(this, 28, 8);
        this.body.addCuboid(-4.0F, -8.0F, -7.0F, 8, 16, 6, 3.75F);
        this.body.setPivot(0.0F, 5.0F, 2.0F);

        this.backRightLeg = new ModelPart(this, 0, 16);
        this.backRightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
        this.backRightLeg.setPivot(-3.0F, 12.0F, 7.0F);

        this.backLeftLeg = new ModelPart(this, 0, 16);
        this.backLeftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
        this.backLeftLeg.setPivot(3.0F, 12.0F, 7.0F);

        this.frontRightLeg = new ModelPart(this, 0, 16);
        this.frontRightLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
        this.frontRightLeg.setPivot(-3.0F, 12.0F, -5.0F);

        this.frontLeftLeg = new ModelPart(this, 0, 16);
        this.frontLeftLeg.addCuboid(-2.0F, 0.0F, -2.0F, 4, 6, 4, 0.5F);
        this.frontLeftLeg.setPivot(3.0F, 12.0F, -5.0F);
    }

    @Override
    public void animateModel(SheepuffEntity sheepuff, float limbSwing, float limbDistance, float partialTickTime) {
        super.animateModel(sheepuff, limbSwing, limbDistance, partialTickTime);

        this.head.pivotY = 6.0F + sheepuff.getHeadRotationPointY(partialTickTime) * 9.0F;
        this.headRotationAngleX = sheepuff.getHeadRotationAngleX(partialTickTime);
    }

    @Override
    public void setAngles(SheepuffEntity sheepuff, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setAngles(sheepuff, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.head.pitch = this.headRotationAngleX;
    }

}
