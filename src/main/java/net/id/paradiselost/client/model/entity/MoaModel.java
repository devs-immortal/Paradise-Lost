// Made with Model Converter by Globox_Z
// Generate all required imports
// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class MoaModel extends EntityModel<MoaEntity> {
    private final ModelPart beak;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart torso;
    private final ModelPart saddle;
    private final ModelPart chest;
    private final ModelPart left_wing;
    private final ModelPart right_wing;
    private final ModelPart left_leg;
    private final ModelPart left_knee;
    private final ModelPart left_foot;
    private final ModelPart right_leg;
    private final ModelPart right_knee;
    private final ModelPart right_foot;
    private final ModelPart crest_up_r1;
    private final ModelPart tail;

    public MoaModel(ModelPart root) {
        this.torso = root.getChild("torso");
        this.tail = this.torso.getChild("tail");
        this.neck = this.torso.getChild("neck");
        this.head = this.neck.getChild("head");
        this.beak = this.head.getChild("beak");
        this.crest_up_r1 = this.head.getChild("crest_up_r1");
        this.right_leg = this.torso.getChild("right_leg");
        this.right_knee = this.right_leg.getChild("right_knee");
        this.right_foot = this.right_knee.getChild("right_foot");
        this.left_leg = this.torso.getChild("left_leg");
        this.left_knee = this.left_leg.getChild("left_knee");
        this.left_foot = this.left_knee.getChild("left_foot");
        this.right_wing = this.torso.getChild("right_wing");
        this.left_wing = this.torso.getChild("left_wing");
        this.chest = this.torso.getChild("chest");
        this.saddle = this.torso.getChild("saddle");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("torso", ModelPartBuilder.create().uv(0, 50).cuboid(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F), ModelTransform.pivot(0.0F, 17.25F, 0.0F));
        modelPartData1.addChild("saddle", ModelPartBuilder.create().uv(52, 50).cuboid(-5.5F, -12.25F, -6.0F, 11.0F, 8.0F, 13.0F, false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData1.addChild("chest", ModelPartBuilder.create().uv(46, 71).cuboid(-5.0F, -14.75F, 1.5F, 10.0F, 3.0F, 6.0F, false), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        modelPartData1.addChild("left_wing", ModelPartBuilder.create().uv(0, 25).cuboid(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F).uv(0, 74).cuboid(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F).uv(60, 86).cuboid(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F), ModelTransform.of(-4.895F, -11.3526F, -5.7418F, 0.0436F, -0.2618F, -1.3963F));
        modelPartData1.addChild("right_wing", ModelPartBuilder.create().uv(0, 0).cuboid(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F).uv(72, 71).cuboid(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F).uv(56, 86).cuboid(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F), ModelTransform.of(4.895F, -11.3526F, -5.7418F, 0.0436F, 0.2618F, 1.3963F));
        ModelPartData modelPartData2 = modelPartData1.addChild("left_leg", ModelPartBuilder.create().uv(24, 74).cuboid(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F), ModelTransform.of(-4.5F, -4.75F, 0.5F, 0.2618F, 0.0F, 0.0F));
        ModelPartData modelPartData3 = modelPartData2.addChild("left_knee", ModelPartBuilder.create().uv(44, 80).cuboid(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F), ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));
        modelPartData3.addChild("left_foot", ModelPartBuilder.create().uv(12, 84).cuboid(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F).uv(56, 80).cuboid(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F), ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));
        ModelPartData modelPartData4 = modelPartData1.addChild("right_leg", ModelPartBuilder.create().uv(74, 0).cuboid(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F), ModelTransform.of(4.5F, -4.75F, 0.5F, 0.2618F, 0.0F, 0.0F));
        ModelPartData modelPartData5 = modelPartData4.addChild("right_knee", ModelPartBuilder.create().uv(0, 0).cuboid(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F), ModelTransform.of(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));
        modelPartData5.addChild("right_foot", ModelPartBuilder.create().uv(0, 84).cuboid(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F).uv(64, 82).cuboid(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F), ModelTransform.of(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));
        ModelPartData modelPartData6 = modelPartData1.addChild("neck", ModelPartBuilder.create().uv(0, 50).cuboid(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F), ModelTransform.of(0.0F, -12.0F, -6.75F, 0.1309F, 0.0F, 0.0F));
        ModelPartData modelPartData7 = modelPartData6.addChild("head", ModelPartBuilder.create().uv(36, 50).cuboid(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F).uv(0, 24).cuboid(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F).uv(0, 15).cuboid(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F).uv(0, 2).cuboid(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F).uv(73, 75).cuboid(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F), ModelTransform.of(0.0F, -7.5653F, 0.0043F, -0.1309F, 0.0F, 0.0F));
        modelPartData7.addChild("crest_up_r1", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 12.0F), ModelTransform.of(0.0F, -5.9503F, 1.7411F, 0.2182F, 0.0F, 0.0F));
        modelPartData7.addChild("beak", ModelPartBuilder.create().uv(0, 78).cuboid(-2.5F, -0.3F, -4.5F, 5.0F, 1.0F, 5.0F), ModelTransform.pivot(0.0F, -0.7003F, -4.0089F));
        modelPartData1.addChild("tail", ModelPartBuilder.create().uv(38, 0).cuboid(-5.0F, -11.75F, 8.0F, 10.0F, 0.0F, 16.0F).uv(54, 25).cuboid(5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).uv(54, 17).cuboid(-5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).uv(54, 0).cuboid(3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).uv(54, 9).cuboid(-3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).uv(0, 40).cuboid(-5.0F, -11.75F, 15.0F, 10.0F, 8.0F, 0.0F), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
        return TexturedModelData.of(modelData, 128, 128);
    }

    @Override
    public void setAngles(MoaEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        saddle.visible = entity.isSaddled();
        chest.visible = entity.hasChest();
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
