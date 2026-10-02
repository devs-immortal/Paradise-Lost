package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SentinelEntityModel<S extends BipedEntityRenderState> extends BipedEntityModel<S> {
    public SentinelEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setAngles(S state) {
        this.resetTransforms();
        boolean gliding = state.isGliding;
        boolean swimming = state.isSwimming;
        this.head.yaw = state.yawDegrees * (float) (Math.PI / 180.0);
        if (gliding) {
            this.head.pitch = (float) (-Math.PI / 4);
        } else if (state.leaningPitch > 0.0F) {
            if (swimming) {
                this.head.pitch = MathHelper.lerpAngleRadians(state.leaningPitch, this.head.pitch, (float) (-Math.PI / 4));
            } else {
                this.head.pitch = MathHelper.lerpAngleRadians(state.leaningPitch, this.head.pitch, state.pitch * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.pitch = state.pitch * (float) (Math.PI / 180.0);
        }

        this.body.yaw = 0.0F;
        this.rightArm.pivotZ = 0.0F;
        this.rightArm.pivotX = -5.0F;
        this.leftArm.pivotZ = 0.0F;
        this.leftArm.pivotX = 5.0F;
        float f = state.limbFrequency;
        float g = state.limbAmplitudeMultiplier;
        float k = state.limbAmplitudeInverse;

        this.rightArm.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 2.0F * g * 0.5F / k;
        this.leftArm.pitch = MathHelper.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
        this.rightArm.roll = 0.0F;
        this.leftArm.roll = 0.0F;
        this.rightLeg.pitch = MathHelper.cos(f * 0.6662F) * 1.4F * g / k;
        this.leftLeg.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.4F * g / k;
        this.rightLeg.yaw = 0.005F;
        this.leftLeg.yaw = -0.005F;
        this.rightLeg.roll = 0.005F;
        this.leftLeg.roll = -0.005F;
        if (state.hasVehicle) {
            this.rightArm.pitch += (float) (-Math.PI / 5);
            this.leftArm.pitch += (float) (-Math.PI / 5);
            this.rightLeg.pitch = -1.4137167F;
            this.rightLeg.yaw = (float) (Math.PI / 10);
            this.rightLeg.roll = 0.07853982F;
            this.leftLeg.pitch = -1.4137167F;
            this.leftLeg.yaw = (float) (-Math.PI / 10);
            this.leftLeg.roll = -0.07853982F;
        }

        this.rightArm.yaw = 0.0F;
        this.leftArm.yaw = 0.0F;

        this.animateArms(state, state.age);
    }
}
