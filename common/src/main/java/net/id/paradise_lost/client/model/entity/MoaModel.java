package net.id.paradise_lost.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

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

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();
        PartDefinition modelPartData1 = modelPartData.addOrReplaceChild("torso", CubeListBuilder.create().texOffs(0, 50).addBox(-5.0F, -11.75F, -8.0F, 10.0F, 8.0F, 16.0F), PartPose.offset(0.0F, 17.25F, 0.0F));
        modelPartData1.addOrReplaceChild("saddle", CubeListBuilder.create().texOffs(52, 50).addBox(-5.5F, -12.25F, -6.0F, 11.0F, 8.0F, 13.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
        modelPartData1.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(46, 71).addBox(-5.0F, -14.75F, 1.5F, 10.0F, 3.0F, 6.0F, false), PartPose.offset(0.0F, 0.0F, 0.0F));
        modelPartData1.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(0, 25).addBox(-12.1994F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F).texOffs(0, 74).addBox(-12.1994F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F).texOffs(60, 86).addBox(-13.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(-4.895F, -11.3526F, -5.7418F, 0.0436F, -0.2618F, -1.3963F));
        modelPartData1.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(0, 0).addBox(-2.8006F, -1.297F, 0.9804F, 15.0F, 1.0F, 24.0F).texOffs(72, 71).addBox(-0.8006F, -1.297F, -1.0196F, 13.0F, 2.0F, 2.0F).texOffs(56, 86).addBox(12.1994F, -1.297F, -1.0196F, 1.0F, 2.0F, 1.0F), PartPose.offsetAndRotation(4.895F, -11.3526F, -5.7418F, 0.0436F, 0.2618F, 1.3963F));
        PartDefinition modelPartData2 = modelPartData1.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(24, 74).addBox(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(-4.5F, -4.75F, 0.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition modelPartData3 = modelPartData2.addOrReplaceChild("left_knee", CubeListBuilder.create().texOffs(44, 80).addBox(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));
        modelPartData3.addOrReplaceChild("left_foot", CubeListBuilder.create().texOffs(12, 84).addBox(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F).texOffs(56, 80).addBox(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));
        PartDefinition modelPartData4 = modelPartData1.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(74, 0).addBox(-2.0F, -3.2588F, -2.9659F, 4.0F, 8.0F, 6.0F), PartPose.offsetAndRotation(4.5F, -4.75F, 0.5F, 0.2618F, 0.0F, 0.0F));
        PartDefinition modelPartData5 = modelPartData4.addOrReplaceChild("right_knee", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.8854F, -1.4469F, 3.0F, 9.0F, 3.0F), PartPose.offsetAndRotation(0.0F, 3.4912F, 0.5341F, -0.3927F, 0.0F, 0.0F));
        modelPartData5.addOrReplaceChild("right_foot", CubeListBuilder.create().texOffs(0, 84).addBox(0.5F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F).texOffs(64, 82).addBox(-1.75F, -0.0956F, -3.2723F, 2.0F, 2.0F, 4.0F), PartPose.offsetAndRotation(-0.5F, 6.3646F, 0.0531F, 0.1309F, 0.0F, 0.0F));
        PartDefinition modelPartData6 = modelPartData1.addOrReplaceChild("neck", CubeListBuilder.create().texOffs(0, 50).addBox(-2.0F, -8.8153F, -2.4957F, 4.0F, 11.0F, 4.0F), PartPose.offsetAndRotation(0.0F, -12.0F, -6.75F, 0.1309F, 0.0F, 0.0F));
        PartDefinition modelPartData7 = modelPartData6.addOrReplaceChild("head", CubeListBuilder.create().texOffs(36, 50).addBox(-3.0F, -5.9503F, -4.2589F, 6.0F, 6.0F, 6.0F).texOffs(0, 24).addBox(0.0F, -10.0F, -14.25F, 0.0F, 6.0F, 10.0F).texOffs(0, 15).addBox(3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F).texOffs(0, 2).addBox(-3.0F, -7.9503F, 1.7411F, 0.0F, 9.0F, 10.0F).texOffs(73, 75).addBox(-3.0F, -5.9503F, -9.2589F, 6.0F, 5.0F, 5.0F), PartPose.offsetAndRotation(0.0F, -7.5653F, 0.0043F, -0.1309F, 0.0F, 0.0F));
        modelPartData7.addOrReplaceChild("crest_up_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, 0.0F, 0.0F, 6.0F, 0.0F, 12.0F), PartPose.offsetAndRotation(0.0F, -5.9503F, 1.7411F, 0.2182F, 0.0F, 0.0F));
        modelPartData7.addOrReplaceChild("beak", CubeListBuilder.create().texOffs(0, 78).addBox(-2.5F, -0.3F, -4.5F, 5.0F, 1.0F, 5.0F), PartPose.offset(0.0F, -0.7003F, -4.0089F));
        modelPartData1.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(38, 0).addBox(-5.0F, -11.75F, 8.0F, 10.0F, 0.0F, 16.0F).texOffs(54, 25).addBox(5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).texOffs(54, 17).addBox(-5.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).texOffs(54, 0).addBox(3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).texOffs(54, 9).addBox(-3.0F, -11.75F, 8.0F, 0.0F, 8.0F, 16.0F).texOffs(0, 40).addBox(-5.0F, -11.75F, 15.0F, 10.0F, 8.0F, 0.0F), PartPose.offset(0.0F, 0.0F, 0.0F));
        return LayerDefinition.create(modelData, 128, 128);
    }

    @Override
    public void setupAnim(MoaEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        saddle.visible = entity.isSaddled();
        chest.visible = entity.hasChest();
        float netYaw = netHeadYaw * 0.017453292F;
        head.yRot = netYaw / 4;
        neck.yRot = (netYaw / 4) * 3;
        float speedPitch = (float) Math.min((new Vec3(entity.getDeltaMovement().x(), 0, entity.getDeltaMovement().z()).length() * 1.1F), 1F) + 0.1309F;
        neck.xRot = speedPitch;
        head.xRot = -speedPitch + headPitch * 0.017453F;
        if (!entity.isInAir) {
            limbSwingAmount /= 2;
            right_leg.xRot = Mth.cos(limbSwing * 0.6662F) * 2F * limbSwingAmount + 0.2618F;
            left_leg.xRot = Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 2F * limbSwingAmount + 0.2618F;
        } else {
            left_leg.xRot = entity.getLegPitch();
            right_leg.xRot = left_leg.xRot;
        }
        left_wing.zRot = entity.getWingRoll();
        right_wing.zRot = -left_wing.zRot;
        left_wing.yRot = entity.getWingYaw();
        right_wing.yRot = -left_wing.yRot;
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        torso.render(matrices, vertices, light, overlay);
    }
}
