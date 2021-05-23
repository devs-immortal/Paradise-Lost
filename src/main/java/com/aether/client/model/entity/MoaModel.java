// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.aether.client.model.entity;

import com.aether.entities.passive.MoaEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class MoaModel extends EntityModel<MoaEntity> {

    private final ModelPart beak;
    private final ModelPart neck, head;
    private final ModelPart torso, saddle, chest;
    private final ModelPart left_wing, right_wing;
    private final ModelPart left_leg, left_knee, left_foot;
    private final ModelPart right_leg, right_knee, right_foot;
    private final ModelPart crest_up_r1;
    private final ModelPart tail;

    public MoaModel() {
        textureWidth = 128;
        textureHeight = 128;
        torso = new ModelPart(this);
        torso.setPivot(0.0F, 17.25F, 0.0F);
        torso.setTextureOffset(0, 50).addCuboid(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F, 0.0F, false);

        saddle = new ModelPart(this);
        saddle.setTextureOffset(52, 50).addCuboid(-5.5F, -12.25F, -6.0F, 11.0F, 8.0F, 13.0F, 0.0F, false);
        torso.addChild(saddle);

        chest = new ModelPart(this);
        chest.setTextureOffset(46, 71).addCuboid(-5.0F, -14.75F, 1.5F, 10.0F, 3.0F, 6.0F, 0.0F, false);
        torso.addChild(chest);

        left_wing = new ModelPart(this);
        left_wing.setPivot(-4.895F, -11.3526F, -5.7418F);
        torso.addChild(left_wing);
        setRotationAngle(left_wing, 0.0436F, -0.2618F, -1.3963F);
        left_wing.setTextureOffset(0, 25).addCuboid(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F, 0.0F, false);
        left_wing.setTextureOffset(0, 74).addCuboid(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F, 0.0F, false);
        left_wing.setTextureOffset(60, 86).addCuboid(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        right_wing = new ModelPart(this);
        right_wing.setPivot(4.895F, -11.3526F, -5.7418F);
        torso.addChild(right_wing);
        setRotationAngle(right_wing, 0.0436F, 0.2618F, 1.3963F);
        right_wing.setTextureOffset(0, 0).addCuboid(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F, 0.0F, false);
        right_wing.setTextureOffset(72, 71).addCuboid(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F, 0.0F, false);
        right_wing.setTextureOffset(56, 86).addCuboid(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F, 0.0F, false);

        left_leg = new ModelPart(this);
        left_leg.setPivot(-4.5F, -4.75F, 0.5F);
        torso.addChild(left_leg);
        setRotationAngle(left_leg, 0.2618F, 0.0F, 0.0F);
        left_leg.setTextureOffset(24, 74).addCuboid(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        left_knee = new ModelPart(this);
        left_knee.setPivot(0.0F, 3.4912F, 0.5341F);
        left_leg.addChild(left_knee);
        setRotationAngle(left_knee, -0.3927F, 0.0F, 0.0F);
        left_knee.setTextureOffset(44, 80).addCuboid(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        left_foot = new ModelPart(this);
        left_foot.setPivot(-0.5F, 6.3646F, 0.0531F);
        left_knee.addChild(left_foot);
        setRotationAngle(left_foot, 0.1309F, 0.0F, 0.0F);
        left_foot.setTextureOffset(12, 84).addCuboid(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        left_foot.setTextureOffset(56, 80).addCuboid(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        right_leg = new ModelPart(this);
        right_leg.setPivot(4.5F, -4.75F, 0.5F);
        torso.addChild(right_leg);
        setRotationAngle(right_leg, 0.2618F, 0.0F, 0.0F);
        right_leg.setTextureOffset(74, 0).addCuboid(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F, 0.0F, false);

        right_knee = new ModelPart(this);
        right_knee.setPivot(0.0F, 3.4912F, 0.5341F);
        right_leg.addChild(right_knee);
        setRotationAngle(right_knee, -0.3927F, 0.0F, 0.0F);
        right_knee.setTextureOffset(0, 0).addCuboid(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F, 0.0F, false);

        right_foot = new ModelPart(this);
        right_foot.setPivot(-0.5F, 6.3646F, 0.0531F);
        right_knee.addChild(right_foot);
        setRotationAngle(right_foot, 0.1309F, 0.0F, 0.0F);
        right_foot.setTextureOffset(0, 84).addCuboid(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);
        right_foot.setTextureOffset(64, 82).addCuboid(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F, 0.0F, false);

        neck = new ModelPart(this);
        neck.setPivot(0.0F, -12.0F, -6.75F);
        torso.addChild(neck);
        setRotationAngle(neck, 0.1309F, 0.0F, 0.0F);
        neck.setTextureOffset(0, 50).addCuboid(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F, 0.0F, false);

        head = new ModelPart(this);
        head.setPivot(0.0F, -7.5653F, 0.0043F);
        neck.addChild(head);
        setRotationAngle(head, -0.1309F, 0.0F, 0.0F);
        head.setTextureOffset(36, 50).addCuboid(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F, 0.0F, false);
        head.setTextureOffset(0, 24).addCuboid(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F, 0.0F, false);
        head.setTextureOffset(0, 15).addCuboid(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F, 0.0F, false);
        head.setTextureOffset(0, 2).addCuboid(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F, 0.0F, false);
        head.setTextureOffset(73, 75).addCuboid(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F, 0.0F, false);

        crest_up_r1 = new ModelPart(this);
        crest_up_r1.setPivot(0.0F, -5.9503F, 1.7411F);
        head.addChild(crest_up_r1);
        setRotationAngle(crest_up_r1, 0.2182F, 0.0F, 0.0F);
        crest_up_r1.setTextureOffset(0, 0).addCuboid(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 12.0F, 0.0F, false);

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
    public void setAngles(MoaEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

        saddle.visible = entity.isSaddled();
        chest.visible = entity.isSaddled();

        float netYaw = netHeadYaw * 0.017453292F;
        head.yaw = netYaw / 4;
        neck.yaw = (netYaw / 4) * 3;

        float speedPitch = (float) Math.min((new Vec3d(entity.getVelocity().getX(), 0, entity.getVelocity().getZ()).length() * 1.1F), 1F) + 0.1309F;

        neck.pitch = speedPitch;
        head.pitch = -speedPitch + headPitch * 0.017453F;


        if (!entity.isGliding()) {
            limbSwingAmount /= 2;

            right_leg.pitch = MathHelper.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount + 0.2618F;
            left_leg.pitch = MathHelper.cos(limbSwing * 0.6662F + 3.1415927F) * 2F * limbSwingAmount + 0.2618F;
        } else {
            left_leg.pitch = entity.getLegPitch();
            right_leg.pitch = left_leg.pitch;
        }

        left_wing.roll = entity.getWingRoll();
        right_wing.roll = -left_wing.roll;

        left_wing.yaw = entity.getWingYaw();
        right_wing.yaw = -left_wing.yaw;
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        torso.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}