package com.aether.client.model;

import com.aether.entities.passive.SheepuffEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.QuadrupedEntityModel;

public class SheepuffWoolModel extends QuadrupedEntityModel<SheepuffEntity> {

    private float headRotationAngleX;

    public SheepuffWoolModel(float scale) {
        this(12, scale, false, 10.0F, 4.0F, 2.0F, 2.0F, 24);
    }

    public SheepuffWoolModel(int legHeight, float scale, boolean headScaled, float childHeadYOffset, float childHeadZOffset, float invertedChildHeadScale, float invertedChildBodyScale, int childBodyYOffset) {
        super(legHeight, scale, headScaled, childHeadYOffset, childHeadZOffset, invertedChildHeadScale, invertedChildBodyScale, childBodyYOffset);

        this.head = new ModelPart(this, 0, 0);
        this.head.addCuboid(-3.0F, -4.0F, -6.0F, 6, 6, 8, 0.0F);
        this.head.setPivot(0.0F, 6.0F, -8.0F);
        this.torso = new ModelPart(this, 28, 8);
        this.torso.addCuboid(-4.0F, -10.0F, -7.0F, 8, 16, 6, 0.0F);
        this.torso.setPivot(0.0F, 5.0F, 2.0F);
    }

    @Override
    public void animateModel(SheepuffEntity sheepuff, float limbSwing, float prevLimbSwing, float partialTickTime) {
        super.animateModel(sheepuff, limbSwing, prevLimbSwing, partialTickTime);

        this.head.pivotY = 6.0F + sheepuff.getHeadRotationPointY(partialTickTime) * 9.0F;
        this.headRotationAngleX = sheepuff.getHeadRotationAngleX(partialTickTime);
    }

    @Override
    public void setAngles(SheepuffEntity sheepuff, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        super.setAngles(sheepuff, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

        this.head.pitch = this.headRotationAngleX;
    }
}
