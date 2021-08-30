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
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CockatriceModelNew extends EntityModel<CockatriceEntity> {
    private final ModelPart torso;
    private final ModelPart leftWing;
    private final ModelPart rightWing;
    private final ModelPart leftLeg;
    private final ModelPart rightLeg;
    private final ModelPart neck;
    private final ModelPart head;

    public CockatriceModelNew(ModelPart root) {
        this.torso = root.getChild(EntityModelPartNames.BODY);
        this.leftWing = this.torso.getChild(EntityModelPartNames.LEFT_WING);
        this.rightWing = this.torso.getChild(EntityModelPartNames.RIGHT_WING);
        this.leftLeg = torso.getChild(EntityModelPartNames.LEFT_LEG);
        this.rightLeg = torso.getChild(EntityModelPartNames.RIGHT_LEG);
        this.neck = this.torso.getChild(EntityModelPartNames.NECK);
        this.head = neck.getChild(EntityModelPartNames.HEAD);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData root = modelData.getRoot();
        ModelPartBuilder torsoBuilder = ModelPartBuilder.create();
        torsoBuilder.uv(0, 50).cuboid(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F);
        ModelPartData torso = root.addChild(EntityModelPartNames.BODY, torsoBuilder, ModelTransform.of(0.0F, 14.0F, -5.0F, -0.6545F, 0.0F, 0.0F));

        ModelPartBuilder leftWingBuilder = ModelPartBuilder.create();
        leftWingBuilder.uv(0, 25).cuboid(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F);
        leftWingBuilder.uv(0, 74).cuboid(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F);
        leftWingBuilder.uv(60, 86).cuboid(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F);
        torso.addChild(EntityModelPartNames.LEFT_WING, leftWingBuilder, ModelTransform.of(-4.895F, -11.3526F, -5.7418F, 0.225F, 0.1531F, -1.3348F));

        ModelPartBuilder rightWingBuilder = ModelPartBuilder.create();
        rightWingBuilder.uv(0, 0).cuboid(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F);
        rightWingBuilder.uv(72, 71).cuboid(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F);
        rightWingBuilder.uv(56, 86).cuboid(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F);
        torso.addChild(EntityModelPartNames.RIGHT_WING, rightWingBuilder, ModelTransform.of(4.895F, -11.3526F, -5.7418F, 0.225F, -0.1531F, 1.3348F));

        ModelPartBuilder leftLegBuilder = ModelPartBuilder.create();
        leftLegBuilder.uv(24, 74).cuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F);
        ModelPartData leftLeg = torso.addChild(EntityModelPartNames.LEFT_LEG, leftLegBuilder, ModelTransform.of(-4.5F, -4.75F, 0.5F, 0.9163F, 0.0F, 0.0F));

        ModelPartBuilder leftKneeBuilder = ModelPartBuilder.create();
        leftKneeBuilder.uv(44, 80).cuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F);
        ModelPartData leftKnee = leftLeg.addChild("left_knee", leftKneeBuilder, ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));

        ModelPartBuilder leftFootBuilder = ModelPartBuilder.create();
        leftFootBuilder.uv(12, 84).cuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        leftFootBuilder.uv(56, 80).cuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        leftKnee.addChild("left_foot", leftFootBuilder, ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));

        ModelPartBuilder rightLegBuilder = ModelPartBuilder.create();
        rightLegBuilder.uv(74, 0).cuboid(-2.0F, -0.8093F, -1.5517F, 4.0F, 8.0F, 6.0F);
        ModelPartData rightLeg = torso.addChild(EntityModelPartNames.RIGHT_LEG, rightLegBuilder, ModelTransform.of(4.5F, -4.75F, 0.5F, 0.9163F, 0.0F, 0.0F));

        ModelPartBuilder rightKneeBuilder = ModelPartBuilder.create();
        rightKneeBuilder.uv(0, 0).cuboid(-1.5F, 0.8364F, 0.7971F, 3.0F, 9.0F, 3.0F);
        ModelPartData rightKnee = rightLeg.addChild("right_knee", rightKneeBuilder, ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));

        ModelPartBuilder rightFootBuilder = ModelPartBuilder.create();
        rightFootBuilder.uv(0, 84).cuboid(0.5F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        rightFootBuilder.uv(64, 82).cuboid(-1.75F, 1.9044F, -1.2723F, 2.0F, 2.0F, 4.0F);
        rightKnee.addChild("right_foot", rightFootBuilder, ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));

        ModelPartBuilder neckBuilder = ModelPartBuilder.create();
        neckBuilder.uv(0, 50).cuboid(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F);
        ModelPartData neck = torso.addChild(EntityModelPartNames.NECK, neckBuilder, ModelTransform.of(0.0F, -11.0F, -6.75F, 0.5236F, 0.0F, 0.0F));

        ModelPartBuilder headBuilder = ModelPartBuilder.create();
        headBuilder.uv(36, 50).cuboid(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F);
        headBuilder.uv(0, 24).cuboid(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F);
        headBuilder.uv(0, 15).cuboid(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F);
        headBuilder.uv(0, 2).cuboid(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F);
        headBuilder.uv(73, 75).cuboid(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F);
        ModelPartData head = neck.addChild(EntityModelPartNames.HEAD, headBuilder, ModelTransform.of(0.0F, -8.0653F, 0.2543F, 0.2182F, 0.0F, 0.0F));

        ModelPartBuilder crest_up_r1 = ModelPartBuilder.create();
        crest_up_r1.uv(0, 0).cuboid(-3.0F, 0.0F, -1.0F, 6.0F, 0.0F, 12.0F);
        head.addChild("crest_up_r1", crest_up_r1, ModelTransform.of(0.0F, -5.9503F, 1.7411F, 0.2182F, 0.0F, 0.0F));

        ModelPartBuilder beak = ModelPartBuilder.create();
        beak.uv(0, 78).cuboid(-2.5F, -0.3F, -4.5F, 5.0F, 1.0F, 5.0F);
        head.addChild(EntityModelPartNames.BEAK, beak, ModelTransform.pivot(0.0F, -0.7003F, -4.0089F));

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
        this.head.pitch = 0.2182F + headPitch * 0.017453292F;
        this.head.yaw = headYaw * 0.017453292F;
        this.rightLeg.pitch = 0.9163F + MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.leftLeg.pitch = 0.9163F + MathHelper.cos(limbAngle * 0.6662F + 3.1415927F) * 1.4F * limbDistance;

        this.leftWing.roll = -1.3348F + animationProgress;
        this.rightWing.roll = 1.3348F - animationProgress;
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