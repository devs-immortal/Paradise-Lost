package com.aether.client.model.entity;

import com.aether.entities.hostile.CockatriceEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class CockatriceModel extends EntityModel<CockatriceEntity> {

	private final ModelPart torso;
	private final ModelPart left_wing;
	private final ModelPart right_wing;
	private final ModelPart left_leg;
	private final ModelPart left_knee;
	private final ModelPart left_foot;
	private final ModelPart right_leg;
	private final ModelPart right_knee;
	private final ModelPart right_foot;
	private final ModelPart neck;
	private final ModelPart head;
	private final ModelPart crest_up_r1;
	private final ModelPart beak;
	private final ModelPart tail;

	public CockatriceModel() {
		textureWidth = 128;
		textureHeight = 128;
		torso = new ModelPart(this);
		torso.setPivot(0.0F, 14.0F, -5.0F);
		setRotationAngle(torso, -0.6545F, 0.0F, 0.0F);
		torso.setTextureOffset(0, 50).addCuboid(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F, 0.0F, false);

		left_wing = new ModelPart(this);
		left_wing.setPivot(-4.895F, -11.3526F, -5.7418F);
		torso.addChild(left_wing);
		setRotationAngle(left_wing, 0.225F, 0.1531F, -1.3348F);
		left_wing.setTextureOffset(0, 25).addCuboid(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F, 0.0F, false);
		left_wing.setTextureOffset(0, 74).addCuboid(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F, 0.0F, false);
		left_wing.setTextureOffset(60, 86).addCuboid(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		right_wing = new ModelPart(this);
		right_wing.setPivot(4.895F, -11.3526F, -5.7418F);
		torso.addChild(right_wing);
		setRotationAngle(right_wing, 0.225F, -0.1531F, 1.3348F);
		right_wing.setTextureOffset(0, 0).addCuboid(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F, 0.0F, false);
		right_wing.setTextureOffset(72, 71).addCuboid(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F, 0.0F, false);
		right_wing.setTextureOffset(56, 86).addCuboid(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F, 0.0F, false);

		left_leg = new ModelPart(this);
		left_leg.setPivot(-4.5F, -4.75F, 0.5F);
		torso.addChild(left_leg);
		setRotationAngle(left_leg, 0.9163F, 0.0F, 0.0F);
		left_leg.setTextureOffset(24, 74).addCuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F, 0.0F, false);

		left_knee = new ModelPart(this);
		left_knee.setPivot(0.0F, 3.4912F, 0.5341F);
		left_leg.addChild(left_knee);
		setRotationAngle(left_knee, -0.3927F, 0.0F, 0.0F);
		left_knee.setTextureOffset(44, 80).addCuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F, 0.0F, false);

		left_foot = new ModelPart(this);
		left_foot.setPivot(-0.5F, 6.3646F, 0.0531F);
		left_knee.addChild(left_foot);
		setRotationAngle(left_foot, 0.1309F, 0.0F, 0.0F);
		left_foot.setTextureOffset(12, 84).addCuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		left_foot.setTextureOffset(56, 80).addCuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);

		right_leg = new ModelPart(this);
		right_leg.setPivot(4.5F, -4.75F, 0.5F);
		torso.addChild(right_leg);
		setRotationAngle(right_leg, 0.9163F, 0.0F, 0.0F);
		right_leg.setTextureOffset(74, 0).addCuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F, 0.0F, false);

		right_knee = new ModelPart(this);
		right_knee.setPivot(0.0F, 3.4912F, 0.5341F);
		right_leg.addChild(right_knee);
		setRotationAngle(right_knee, -0.3927F, 0.0F, 0.0F);
		right_knee.setTextureOffset(0, 0).addCuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F, 0.0F, false);

		right_foot = new ModelPart(this);
		right_foot.setPivot(-0.5F, 6.3646F, 0.0531F);
		right_knee.addChild(right_foot);
		setRotationAngle(right_foot, 0.1309F, 0.0F, 0.0F);
		right_foot.setTextureOffset(0, 84).addCuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);
		right_foot.setTextureOffset(64, 82).addCuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);

		neck = new ModelPart(this);
		neck.setPivot(0.0F, -11.0F, -6.75F);
		torso.addChild(neck);
		setRotationAngle(neck, 0.5236F, 0.0F, 0.0F);
		neck.setTextureOffset(0, 50).addCuboid(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -8.0653F, 0.2543F);
		neck.addChild(head);
		setRotationAngle(head, 0.2182F, 0.0F, 0.0F);
		head.setTextureOffset(36, 50).addCuboid(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(0, 24).addCuboid(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F, 0.0F, false);
		head.setTextureOffset(0, 15).addCuboid(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F, 0.0F, false);
		head.setTextureOffset(0, 2).addCuboid(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F, 0.0F, false);
		head.setTextureOffset(73, 75).addCuboid(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F, 0.0F, false);

		crest_up_r1 = new ModelPart(this);
		crest_up_r1.setPivot(0.0F, -5.9503F, 1.7411F);
		head.addChild(crest_up_r1);
		setRotationAngle(crest_up_r1, 0.2182F, 0.0F, 0.0F);
		crest_up_r1.setTextureOffset(0, 0).addCuboid(-3.0F, 0.0F, -1.0F, 6.0F, 0.0F, 12.0F, 0.0F, false);

		beak = new ModelPart(this);
		beak.setPivot(0.0F, -0.7003F, -4.0089F);
		head.addChild(beak);
		beak.setTextureOffset(0, 78).addCuboid(-2.5F, -0.3F, -4.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);

		tail = new ModelPart(this);
		tail.setPivot(0.0F, 0.0F, 0.0F);
		torso.addChild(tail);
		tail.setTextureOffset(38, 0).addCuboid(-5.0F, -11.75F, 8.0F, 10.0F, 0.0F, 16.0F, 0.0F, false);
		tail.setTextureOffset(54, 25).addCuboid(5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F, 0.0F, false);
		tail.setTextureOffset(54, 17).addCuboid(-5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F, 0.0F, false);
		tail.setTextureOffset(54, 0).addCuboid(3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F, 0.0F, false);
		tail.setTextureOffset(54, 9).addCuboid(-3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F, 0.0F, false);
		tail.setTextureOffset(0, 40).addCuboid(-5.0F, -11.75F, 15.0F, 10.0F, 8.0F, 0.0F, 0.0F, false);
	}

	@Override
	public void setAngles(CockatriceEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		//previously the render function, render code was moved to a method below
	}
	@Override
	public void render(MatrixStack matrixStack, VertexConsumer	buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
		torso.render(matrixStack, buffer, packedLight, packedOverlay);
	}
	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

}