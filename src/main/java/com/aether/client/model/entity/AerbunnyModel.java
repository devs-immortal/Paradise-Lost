// Made with Blockbench 3.8.3
	// Exported for Minecraft version 1.15
	// Paste this class into your mod and generate all required imports

	package com.aether.client.model.entity;

import com.aether.entities.passive.AerbunnyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class AerbunnyModel extends EntityModel<AerbunnyEntity> {

	private final ModelPart body;
	private final ScaledModelPart fluff;
	private final ModelPart head;
	private final ModelPart left_front_leg;
	private final ModelPart right_front_leg;
	private final ModelPart back_right_leg;
	private final ModelPart back_left_leg;
	private final ModelPart tail;


	public AerbunnyModel() {
		textureWidth = 32;
		textureHeight = 32;
		body = new ModelPart(this);
		body.setPivot(0.0F, 22.8889F, 1.0F);


		fluff = new ScaledModelPart(this);
		fluff.setPivot(0.0F, 0F, 0F);
		fluff.setTextureOffset(0, 0).addCuboid(-4.0F, -3.5F, -3.5F, 8.0F, 7.0F, 7.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -2.3889F, -4.5F);
		body.addChild(head);
		head.setTextureOffset(18, 26).addCuboid(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 3.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addCuboid(-2.0F, -5.5F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addCuboid(1.0F, -5.5F, -2.0F, 1.0F, 4.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(0, 14).addCuboid(-4.0F, -0.5F, -2.0F, 2.0F, 2.0F, 0.0F, 0.0F, false);
		head.setTextureOffset(0, 14).addCuboid(2.0F, -0.5F, -2.0F, 2.0F, 2.0F, 0.0F, 0.0F, true);

		left_front_leg = new ModelPart(this);
		left_front_leg.setPivot(-4.0F, 1.1111F, -3.25F);
		body.addChild(left_front_leg);
		left_front_leg.setTextureOffset(0, 21).addCuboid(-1.0F, -1.0F, -2.25F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		right_front_leg = new ModelPart(this);
		right_front_leg.setPivot(4.0F, 1.1111F, -3.0F);
		body.addChild(right_front_leg);
		right_front_leg.setTextureOffset(0, 21).addCuboid(-1.0F, -1.0F, -2.5F, 2.0F, 1.0F, 3.0F, 0.0F, false);

		back_right_leg = new ModelPart(this);
		back_right_leg.setPivot(4.0F, 0.1111F, 2.5F);
		body.addChild(back_right_leg);
		back_right_leg.setTextureOffset(10, 20).addCuboid(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);
		back_right_leg.setTextureOffset(22, 20).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		back_left_leg = new ModelPart(this);
		back_left_leg.setPivot(-4.0F, 0.1111F, 2.5F);
		body.addChild(back_left_leg);
		back_left_leg.setTextureOffset(22, 20).addCuboid(-1.0F, -2.0F, -2.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		back_left_leg.setTextureOffset(10, 20).addCuboid(-1.0F, 0.0F, -3.0F, 2.0F, 1.0F, 4.0F, 0.0F, false);

		tail = new ModelPart(this);
		tail.setPivot(0.0F, -2.8889F, 2.5F);
		body.addChild(tail);
		tail.setTextureOffset(0, 25).addCuboid(-2.0F, -2.0F, 0.0F, 4.0F, 4.0F, 3.0F, 0.0F, false);
	}

	@Override
	public void setAngles(AerbunnyEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch){
		head.pitch = headPitch * 0.017453F;
		head.yaw = netHeadYaw * 0.017453292F;
		back_right_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		back_left_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		right_front_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 1.4F * limbSwingAmount;
		left_front_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;

		float targetFloof = entity.getPuffiness() / 2F;

		if(entity.floof < targetFloof) {
			entity.floof += 0.025F;
		}
		else if(entity.floof > targetFloof) {
			entity.floof -= 0.025F;
		}

		if(Math.abs(targetFloof - entity.floof) <= 0.03)
			entity.floof = targetFloof;

		fluff.uniformScale(entity.floof + 1);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha){
			body.render(matrixStack, buffer, packedLight, packedOverlay);
			matrixStack.translate(0, 1.28, 0);
			matrixStack.scale(fluff.scaleX, fluff.scaleY, fluff.scaleZ);
			fluff.render(matrixStack, buffer, packedLight, packedOverlay);
	}
}