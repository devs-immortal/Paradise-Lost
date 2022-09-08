package net.id.aether.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.passive.ambyst.AmbystEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.EntityModelPartNames;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class AmbystModel extends AnimalModel<AmbystEntity> {

    public static final float MOVING_IN_WATER_LEG_PITCH = 1.8849558F;
    private final ModelPart tail;
    private final ModelPart leftHindLeg;
    private final ModelPart rightHindLeg;
    private final ModelPart leftFrontLeg;
    private final ModelPart rightFrontLeg;
    private final ModelPart body;
    private final ModelPart head;
    private final ModelPart topGills;
    private final ModelPart leftGills;
    private final ModelPart rightGills;

    public AmbystModel(ModelPart root) {
        super(true, 8.0F, 3.35F);
        this.body = root.getChild(EntityModelPartNames.BODY);
        this.head = this.body.getChild(EntityModelPartNames.HEAD);
        this.rightHindLeg = this.body.getChild(EntityModelPartNames.RIGHT_HIND_LEG);
        this.leftHindLeg = this.body.getChild(EntityModelPartNames.LEFT_HIND_LEG);
        this.rightFrontLeg = this.body.getChild(EntityModelPartNames.RIGHT_FRONT_LEG);
        this.leftFrontLeg = this.body.getChild(EntityModelPartNames.LEFT_FRONT_LEG);
        this.tail = this.body.getChild(EntityModelPartNames.TAIL);
        this.topGills = this.head.getChild(EntityModelPartNames.TOP_GILLS);
        this.leftGills = this.head.getChild(EntityModelPartNames.LEFT_GILLS);
        this.rightGills = this.head.getChild(EntityModelPartNames.RIGHT_GILLS);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData2 = modelPartData.addChild(EntityModelPartNames.BODY, ModelPartBuilder.create().uv(0, 11).cuboid(-4.0F, -2.0F, -9.0F, 8.0F, 4.0F, 10.0F).uv(2, 17).cuboid(0.0F, -3.0F, -8.0F, 0.0F, 5.0F, 9.0F), ModelTransform.pivot(0.0F, 20.0F, 5.0F));
        Dilation dilation = new Dilation(0.001F);
        ModelPartData modelPartData3 = modelPartData2.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create().uv(0, 1).cuboid(-4.0F, -3.0F, -5.0F, 8.0F, 5.0F, 5.0F, dilation), ModelTransform.pivot(0.0F, 0.0F, -9.0F));
        ModelPartBuilder modelPartBuilder = ModelPartBuilder.create().uv(3, 37).cuboid(-4.0F, -3.0F, 0.0F, 8.0F, 3.0F, 0.0F, dilation);
        ModelPartBuilder modelPartBuilder2 = ModelPartBuilder.create().uv(0, 40).cuboid(-3.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, dilation);
        ModelPartBuilder modelPartBuilder3 = ModelPartBuilder.create().uv(11, 40).cuboid(0.0F, -5.0F, 0.0F, 3.0F, 7.0F, 0.0F, dilation);
        modelPartData3.addChild(EntityModelPartNames.TOP_GILLS, modelPartBuilder, ModelTransform.pivot(0.0F, -3.0F, -1.0F));
        modelPartData3.addChild(EntityModelPartNames.LEFT_GILLS, modelPartBuilder2, ModelTransform.pivot(-4.0F, 0.0F, -1.0F));
        modelPartData3.addChild(EntityModelPartNames.RIGHT_GILLS, modelPartBuilder3, ModelTransform.pivot(4.0F, 0.0F, -1.0F));
        ModelPartBuilder modelPartBuilder4 = ModelPartBuilder.create().uv(2, 13).cuboid(-1.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, dilation);
        ModelPartBuilder modelPartBuilder5 = ModelPartBuilder.create().uv(2, 13).cuboid(-2.0F, 0.0F, 0.0F, 3.0F, 5.0F, 0.0F, dilation);
        modelPartData2.addChild(EntityModelPartNames.RIGHT_HIND_LEG, modelPartBuilder5, ModelTransform.pivot(-3.5F, 1.0F, -1.0F));
        modelPartData2.addChild(EntityModelPartNames.LEFT_HIND_LEG, modelPartBuilder4, ModelTransform.pivot(3.5F, 1.0F, -1.0F));
        modelPartData2.addChild(EntityModelPartNames.RIGHT_FRONT_LEG, modelPartBuilder5, ModelTransform.pivot(-3.5F, 1.0F, -8.0F));
        modelPartData2.addChild(EntityModelPartNames.LEFT_FRONT_LEG, modelPartBuilder4, ModelTransform.pivot(3.5F, 1.0F, -8.0F));
        modelPartData2.addChild(EntityModelPartNames.TAIL, ModelPartBuilder.create().uv(2, 19).cuboid(0.0F, -3.0F, 0.0F, 0.0F, 5.0F, 12.0F), ModelTransform.pivot(0.0F, 0.0F, 1.0F));
        return TexturedModelData.of(modelData, 64, 64);
    }

    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body);
    }

    public void setAngles(AmbystEntity axolotlEntity, float f, float g, float h, float i, float j) {
        this.resetAngles(axolotlEntity, i, j);
        if (true == false/*axolotlEntity.isPlayingDead()*/) {
            this.setPlayingDeadAngles(i);
            this.updateAnglesCache(axolotlEntity);
        } else {
            boolean bl = axolotlEntity.getVelocity().horizontalLengthSquared() > 1.0E-7D || axolotlEntity.getPitch() != axolotlEntity.prevPitch || axolotlEntity.getYaw() != axolotlEntity.prevYaw || axolotlEntity.lastRenderX != axolotlEntity.getX() || axolotlEntity.lastRenderZ != axolotlEntity.getZ();
            if (axolotlEntity.isInsideWaterOrBubbleColumn()) {
                if (bl) {
                    this.setMovingInWaterAngles(h, j);
                } else {
                    this.setStandingInWaterAngles(h);
                }

                this.updateAnglesCache(axolotlEntity);
            } else {
                if (axolotlEntity.isOnGround()) {
                    if (bl) {
                        this.setMovingOnGroundAngles(h, i);
                    } else {
                        this.setStandingOnGroundAngles(h, i);
                    }
                }

                this.updateAnglesCache(axolotlEntity);
            }
        }
    }

    private void updateAnglesCache(AmbystEntity ambyst) {
        Map<String, Vec3f> map = ambyst.getModelAngles();
        map.put("body", this.getAngles(this.body));
        map.put("head", this.getAngles(this.head));
        map.put("right_hind_leg", this.getAngles(this.rightHindLeg));
        map.put("left_hind_leg", this.getAngles(this.leftHindLeg));
        map.put("right_front_leg", this.getAngles(this.rightFrontLeg));
        map.put("left_front_leg", this.getAngles(this.leftFrontLeg));
        map.put("tail", this.getAngles(this.tail));
        map.put("top_gills", this.getAngles(this.topGills));
        map.put("left_gills", this.getAngles(this.leftGills));
        map.put("right_gills", this.getAngles(this.rightGills));
    }

    private Vec3f getAngles(ModelPart part) {
        return new Vec3f(part.pitch, part.yaw, part.roll);
    }

    private void setAngles(ModelPart part, Vec3f angles) {
        part.setAngles(angles.getX(), angles.getY(), angles.getZ());
    }

    /**
     * Resets the angles of the axolotl model.
     */
    private void resetAngles(AmbystEntity ambyst, float headYaw, float headPitch) {
        this.body.pivotX = 0.0F;
        this.head.pivotY = 0.0F;
        this.body.pivotY = 20.0F;
        Map<String, Vec3f> map = ambyst.getModelAngles();
        if (map.isEmpty()) {
            this.body.setAngles(headPitch * 0.017453292F, headYaw * 0.017453292F, 0.0F);
            this.head.setAngles(0.0F, 0.0F, 0.0F);
            this.leftHindLeg.setAngles(0.0F, 0.0F, 0.0F);
            this.rightHindLeg.setAngles(0.0F, 0.0F, 0.0F);
            this.leftFrontLeg.setAngles(0.0F, 0.0F, 0.0F);
            this.rightFrontLeg.setAngles(0.0F, 0.0F, 0.0F);
            this.leftGills.setAngles(0.0F, 0.0F, 0.0F);
            this.rightGills.setAngles(0.0F, 0.0F, 0.0F);
            this.topGills.setAngles(0.0F, 0.0F, 0.0F);
            this.tail.setAngles(0.0F, 0.0F, 0.0F);
        } else {
            this.setAngles(this.body, map.get("body"));
            this.setAngles(this.head, map.get("head"));
            this.setAngles(this.leftHindLeg, map.get("left_hind_leg"));
            this.setAngles(this.rightHindLeg, map.get("right_hind_leg"));
            this.setAngles(this.leftFrontLeg, map.get("left_front_leg"));
            this.setAngles(this.rightFrontLeg, map.get("right_front_leg"));
            this.setAngles(this.leftGills, map.get("left_gills"));
            this.setAngles(this.rightGills, map.get("right_gills"));
            this.setAngles(this.topGills, map.get("top_gills"));
            this.setAngles(this.tail, map.get("tail"));
        }

    }

    private float lerpAngleDegrees(float start, float end) {
        return this.lerpAngleDegrees(0.05F, start, end);
    }

    private float lerpAngleDegrees(float delta, float start, float end) {
        return MathHelper.lerpAngleDegrees(delta, start, end);
    }

    private void setAngles(ModelPart part, float pitch, float yaw, float roll) {
        part.setAngles(this.lerpAngleDegrees(part.pitch, pitch), this.lerpAngleDegrees(part.yaw, yaw), this.lerpAngleDegrees(part.roll, roll));
    }

    private void setStandingOnGroundAngles(float animationProgress, float headYaw) {
        float f = animationProgress * 0.09F;
        float g = MathHelper.sin(f);
        float h = MathHelper.cos(f);
        float i = g * g - 2.0F * g;
        float j = h * h - 3.0F * g;
        this.head.pitch = this.lerpAngleDegrees(this.head.pitch, -0.09F * i);
        this.head.yaw = this.lerpAngleDegrees(this.head.yaw, 0.0F);
        this.head.roll = this.lerpAngleDegrees(this.head.roll, -0.2F);
        this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, -0.1F + 0.1F * i);
        this.topGills.pitch = this.lerpAngleDegrees(this.topGills.pitch, 0.6F + 0.05F * j);
        this.leftGills.yaw = this.lerpAngleDegrees(this.leftGills.yaw, -this.topGills.pitch);
        this.rightGills.yaw = this.lerpAngleDegrees(this.rightGills.yaw, -this.leftGills.yaw);
        this.setAngles(this.leftHindLeg, 1.1F, 1.0F, 0.0F);
        this.setAngles(this.leftFrontLeg, 0.8F, 2.3F, -0.5F);
        this.copyLegAngles();
        this.body.pitch = this.lerpAngleDegrees(0.2F, this.body.pitch, 0.0F);
        this.body.yaw = this.lerpAngleDegrees(this.body.yaw, headYaw * 0.017453292F);
        this.body.roll = this.lerpAngleDegrees(this.body.roll, 0.0F);
    }

    private void setMovingOnGroundAngles(float animationProgress, float headYaw) {
        float f = animationProgress * 0.11F;
        float g = MathHelper.cos(f);
        float h = (g * g - 2.0F * g) / 5.0F;
        float i = 0.7F * g;
        this.head.pitch = this.lerpAngleDegrees(this.head.pitch, 0.0F);
        this.head.yaw = this.lerpAngleDegrees(this.head.yaw, 0.09F * g);
        this.head.roll = this.lerpAngleDegrees(this.head.roll, 0.0F);
        this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, this.head.yaw);
        this.topGills.pitch = this.lerpAngleDegrees(this.topGills.pitch, 0.6F - 0.08F * (g * g + 2.0F * MathHelper.sin(f)));
        this.leftGills.yaw = this.lerpAngleDegrees(this.leftGills.yaw, -this.topGills.pitch);
        this.rightGills.yaw = this.lerpAngleDegrees(this.rightGills.yaw, -this.leftGills.yaw);
        this.setAngles(this.leftHindLeg, 0.9424779F, 1.5F - h, -0.1F);
        this.setAngles(this.leftFrontLeg, 1.0995574F, 1.5707964F - i, 0.0F);
        this.setAngles(this.rightHindLeg, this.leftHindLeg.pitch, -1.0F - h, 0.0F);
        this.setAngles(this.rightFrontLeg, this.leftFrontLeg.pitch, -1.5707964F - i, 0.0F);
        this.body.pitch = this.lerpAngleDegrees(0.2F, this.body.pitch, 0.0F);
        this.body.yaw = this.lerpAngleDegrees(this.body.yaw, headYaw * 0.017453292F);
        this.body.roll = this.lerpAngleDegrees(this.body.roll, 0.0F);
    }

    private void setStandingInWaterAngles(float animationProgress) {
        float f = animationProgress * 0.075F;
        float g = MathHelper.cos(f);
        float h = MathHelper.sin(f) * 0.15F;
        this.body.pitch = this.lerpAngleDegrees(this.body.pitch, -0.15F + 0.075F * g);
        this.body.pivotY -= h;
        this.head.pitch = this.lerpAngleDegrees(this.head.pitch, -this.body.pitch);
        this.topGills.pitch = this.lerpAngleDegrees(this.topGills.pitch, 0.2F * g);
        this.leftGills.yaw = this.lerpAngleDegrees(this.leftGills.yaw, -0.3F * g - 0.19F);
        this.rightGills.yaw = this.lerpAngleDegrees(this.rightGills.yaw, -this.leftGills.yaw);
        this.setAngles(this.leftHindLeg, 2.3561945F - g * 0.11F, 0.47123894F, 1.7278761F);
        this.setAngles(this.leftFrontLeg, 0.7853982F - g * 0.2F, 2.042035F, 0.0F);
        this.copyLegAngles();
        this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, 0.5F * g);
        this.head.yaw = this.lerpAngleDegrees(this.head.yaw, 0.0F);
        this.head.roll = this.lerpAngleDegrees(this.head.roll, 0.0F);
    }

    private void setMovingInWaterAngles(float animationProgress, float headPitch) {
        float f = animationProgress * 0.33F;
        float g = MathHelper.sin(f);
        float h = MathHelper.cos(f);
        float i = 0.13F * g;
        this.body.pitch = this.lerpAngleDegrees(0.1F, this.body.pitch, headPitch * 0.017453292F + i);
        this.head.pitch = -i * 1.8F;
        this.body.pivotY -= 0.45F * h;
        this.topGills.pitch = this.lerpAngleDegrees(this.topGills.pitch, -0.5F * g - 0.8F);
        this.leftGills.yaw = this.lerpAngleDegrees(this.leftGills.yaw, 0.3F * g + 0.9F);
        this.rightGills.yaw = this.lerpAngleDegrees(this.rightGills.yaw, -this.leftGills.yaw);
        this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, 0.3F * MathHelper.cos(f * 0.9F));
        this.setAngles(this.leftHindLeg, 1.8849558F, -0.4F * g, 1.5707964F);
        this.setAngles(this.leftFrontLeg, 1.8849558F, -0.2F * h - 0.1F, 1.5707964F);
        this.copyLegAngles();
        this.head.yaw = this.lerpAngleDegrees(this.head.yaw, 0.0F);
        this.head.roll = this.lerpAngleDegrees(this.head.roll, 0.0F);
    }

    private void setPlayingDeadAngles(float headYaw) {
        this.setAngles(this.leftHindLeg, 1.4137167F, 1.0995574F, 0.7853982F);
        this.setAngles(this.leftFrontLeg, 0.7853982F, 2.042035F, 0.0F);
        this.body.pitch = this.lerpAngleDegrees(this.body.pitch, -0.15F);
        this.body.roll = this.lerpAngleDegrees(this.body.roll, 0.35F);
        this.copyLegAngles();
        this.body.yaw = this.lerpAngleDegrees(this.body.yaw, headYaw * 0.017453292F);
        this.head.pitch = this.lerpAngleDegrees(this.head.pitch, 0.0F);
        this.head.yaw = this.lerpAngleDegrees(this.head.yaw, 0.0F);
        this.head.roll = this.lerpAngleDegrees(this.head.roll, 0.0F);
        this.tail.yaw = this.lerpAngleDegrees(this.tail.yaw, 0.0F);
        this.setAngles(this.topGills, 0.0F, 0.0F, 0.0F);
        this.setAngles(this.leftGills, 0.0F, 0.0F, 0.0F);
        this.setAngles(this.rightGills, 0.0F, 0.0F, 0.0F);
    }

    /**
     * Copies and mirrors the left leg angles to the right leg angles.
     */
    private void copyLegAngles() {
        this.setAngles(this.rightHindLeg, this.leftHindLeg.pitch, -this.leftHindLeg.yaw, -this.leftHindLeg.roll);
        this.setAngles(this.rightFrontLeg, this.leftFrontLeg.pitch, -this.leftFrontLeg.yaw, -this.leftFrontLeg.roll);
    }
}
