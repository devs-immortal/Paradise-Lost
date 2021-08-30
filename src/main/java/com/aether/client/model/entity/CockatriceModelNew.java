package com.aether.client.model.entity;

import com.aether.entities.hostile.CockatriceEntity;

import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class CockatriceModelNew extends EntityModel<CockatriceEntity> {
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

    public CockatriceModelNew(ModelPart root) {
        this.torso = root.getChild("torso");
        this.left_wing = this.torso.getChild("left_wing");
        this.right_wing = this.torso.getChild("right_wing");
        this.left_leg = torso.getChild("left_leg");
        this.left_knee = left_leg.getChild("left_knee");
        this.left_foot = left_knee.getChild("left_foot");
        this.right_leg = torso.getChild("right_leg");
        this.right_knee = right_leg.getChild("right_knee");
        this.right_foot = right_knee.getChild("right_foot");
        this.neck = this.torso.getChild("neck");
        this.head = neck.getChild("head");
        this.crest_up_r1 = head.getChild("crest_up_r1");
        this.beak = head.getChild("beak");
        this.tail = torso.getChild("tail");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        ModelPartBuilder torsoBuilder = ModelPartBuilder.create();
        torsoBuilder.uv(0, 50).cuboid(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F);
        ModelPartData torso = root.addChild("torso", torsoBuilder, ModelTransform.of(0.0F, 14.0F, -5.0F, -0.6545F, 0.0F, 0.0F));

        ModelPartBuilder leftWingBuilder = ModelPartBuilder.create();
        leftWingBuilder.uv(0, 25).cuboid(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F);
        leftWingBuilder.uv(0, 74).cuboid(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F);
        leftWingBuilder.uv(60, 86).cuboid(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F);
        torso.addChild("left_wing", leftWingBuilder, ModelTransform.of(-4.895F, -11.3526F, -5.7418F, 0.225F, 0.1531F, -1.3348F));

        ModelPartBuilder rightWingBuilder = ModelPartBuilder.create();
        rightWingBuilder.uv(0, 0).cuboid(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F);
        rightWingBuilder.uv(72, 71).cuboid(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F);
        rightWingBuilder.uv(56, 86).cuboid(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F);
        torso.addChild("right_wing", rightWingBuilder, ModelTransform.of(4.895F, -11.3526F, -5.7418F, 0.225F, -0.1531F, 1.3348F));

        ModelPartBuilder leftLegBuilder = ModelPartBuilder.create();
        leftLegBuilder.uv(24, 74).cuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F);
        ModelPartData leftLeg = torso.addChild("left_leg", leftLegBuilder, ModelTransform.of(-4.5F, -4.75F, 0.5F, 0.9163F, 0.0F, 0.0F));

        ModelPartBuilder leftKneeBuilder = ModelPartBuilder.create();
        leftKneeBuilder.uv(44, 80).cuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F);
        ModelPartData leftKnee = leftLeg.addChild("left_knee", leftKneeBuilder, ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));

        ModelPartBuilder leftFootBuilder = ModelPartBuilder.create();
        leftFootBuilder.uv(12, 84).cuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        leftFootBuilder.uv(56, 80).cuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        leftKnee.addChild("left_foot", leftFootBuilder, ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));

        ModelPartBuilder rightLegBuilder = ModelPartBuilder.create();
        rightLegBuilder.uv(74, 0).cuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F);
        ModelPartData rightLeg = torso.addChild("right_leg", rightLegBuilder, ModelTransform.of(4.5F, -4.75F, 0.5F, 0.9163F, 0.0F, 0.0F));

        ModelPartBuilder rightKneeBuilder = ModelPartBuilder.create();
        rightKneeBuilder.uv(0, 0).cuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F);
        ModelPartData rightKnee = rightLeg.addChild("right_knee", rightKneeBuilder, ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));

        ModelPartBuilder rightFootBuilder = ModelPartBuilder.create();
        rightFootBuilder.uv(0, 84).cuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        rightFootBuilder.uv(64, 82).cuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        rightKnee.addChild("right_foot", rightFootBuilder, ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));

        ModelPartBuilder neckBuilder = ModelPartBuilder.create();
        neckBuilder.uv(0, 50).cuboid(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F);
        ModelPartData neck = torso.addChild("neck", neckBuilder, ModelTransform.of(0.0F, -11.0F, -6.75F, 0.5236F, 0.0F, 0.0F));

        ModelPartBuilder headBuilder = ModelPartBuilder.create();
        headBuilder.uv(36, 50).cuboid(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F);
        headBuilder.uv(0, 24).cuboid(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F);
        headBuilder.uv(0, 15).cuboid(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F);
        headBuilder.uv(0, 2).cuboid(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F);
        headBuilder.uv(73, 75).cuboid(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F);
        ModelPartData head = neck.addChild("head", headBuilder, ModelTransform.of(0.0F, -8.0653F, 0.2543F, 0.2182F, 0.0F, 0.0F));

        ModelPartBuilder crest_up_r1 = ModelPartBuilder.create();
        crest_up_r1.uv(0, 0).cuboid(-3.0F, 0.0F, -1.0F, 6.0F, 0.0F, 12.0F);
        head.addChild("crest_up_r1", crest_up_r1, ModelTransform.of(0.0F, -5.9503F, 1.7411F, 0.2182F, 0.0F, 0.0F));

        ModelPartBuilder beak = ModelPartBuilder.create();
        beak.uv(0, 78).cuboid(-2.5F, -0.3F, -4.5F, 5.0F, 1.0F, 5.0F);
        head.addChild("beak", beak, ModelTransform.pivot(0.0F, -0.7003F, -4.0089F));

        ModelPartBuilder tail = ModelPartBuilder.create();
        tail.uv(38, 0).cuboid(-5.0F, -11.75F, 8.0F, 10.0F, 0.0F, 16.0F);
        tail.uv(54, 25).cuboid(5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F);
        tail.uv(54, 17).cuboid(-5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F);
        tail.uv(54, 0).cuboid(3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F);
        tail.uv(54, 9).cuboid(-3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F);
        tail.uv(0, 40).cuboid(-5.0F, -11.75F, 15.0F, 10.0F, 8.0F, 0.0F);
        torso.addChild("tail", tail, ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(CockatriceEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.head.pitch = headPitch / 57.29578F;
        this.head.yaw = headYaw / 57.29578F;
        //        float speedPitch = (float) Math.min((new Vec3d(entity.getVelocity().getX(), 0, entity.getVelocity().getZ()).length() * 1.1F), 1F) + 0.1309F;
        //        neck.pitch = speedPitch;
        //        head.pitch = -speedPitch + headPitch * 0.017453F;
        if (!entity.isGliding()) {
            limbDistance /= 2;
            right_leg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 2F * limbDistance + 0.2618F;
            left_leg.pitch = MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 2F * limbDistance + 0.2618F;
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
        this.torso.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public ModelPart getHead() {
        return this.head;
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }

}