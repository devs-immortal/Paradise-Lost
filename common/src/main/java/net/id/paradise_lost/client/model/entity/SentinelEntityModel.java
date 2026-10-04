package net.id.paradise_lost.client.model.entity;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;

public class SentinelEntityModel<S extends HumanoidRenderState> extends HumanoidModel<S> {
    public SentinelEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public void setupAnim(S state) {
        this.resetPose();
        boolean bl = state.isFallFlying;
        boolean bl2 = state.isVisuallySwimming;
        this.head.yRot = state.yRot * (float) (Math.PI / 180.0);
        if (bl) {
            this.head.xRot = (float) (-Math.PI / 4);
        } else if (state.swimAmount > 0.0F) {
            if (bl2) {
                this.head.xRot = Mth.rotLerpRad(state.swimAmount, this.head.xRot, (float) (-Math.PI / 4));
            } else {
                this.head.xRot = Mth.rotLerpRad(state.swimAmount, this.head.xRot, state.xRot * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.xRot = state.xRot * (float) (Math.PI / 180.0);
        }

        this.body.yRot = 0.0F;
        this.rightArm.z = 0.0F;
        this.rightArm.x = -5.0F;
        this.leftArm.z = 0.0F;
        this.leftArm.x = 5.0F;
        float f = state.walkAnimationPos;
        float g = state.walkAnimationSpeed;
        float k = state.speedValue;
        this.rightArm.xRot = Mth.cos(f * 0.6662F + (float) Math.PI) * 2.0F * g * 0.5F / k;
        this.leftArm.xRot = Mth.cos(f * 0.6662F) * 2.0F * g * 0.5F / k;
        this.rightArm.zRot = 0.0F;
        this.leftArm.zRot = 0.0F;
        this.rightLeg.xRot = Mth.cos(f * 0.6662F) * 1.4F * g / k;
        this.leftLeg.xRot = Mth.cos(f * 0.6662F + (float) Math.PI) * 1.4F * g / k;
        this.rightLeg.yRot = 0.005F;
        this.leftLeg.yRot = -0.005F;
        this.rightLeg.zRot = 0.005F;
        this.leftLeg.zRot = -0.005F;
        if (state.isPassenger) {
            this.rightArm.xRot += (float) (-Math.PI / 5);
            this.leftArm.xRot += (float) (-Math.PI / 5);
            this.rightLeg.xRot = -1.4137167F;
            this.rightLeg.yRot = (float) (Math.PI / 10);
            this.rightLeg.zRot = 0.07853982F;
            this.leftLeg.xRot = -1.4137167F;
            this.leftLeg.yRot = (float) (-Math.PI / 10);
            this.leftLeg.zRot = -0.07853982F;
        }

        this.rightArm.yRot = 0.0F;
        this.leftArm.yRot = 0.0F;
        this.setupAttackAnimation(state, state.ageInTicks);
    }
}
