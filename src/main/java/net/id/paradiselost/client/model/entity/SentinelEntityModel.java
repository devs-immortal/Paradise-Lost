package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class SentinelEntityModel<T extends HostileEntity> extends BipedEntityModel<T> {
    public SentinelEntityModel(ModelPart modelPart) {
        super(modelPart);
    }

    public void setAngles(T livingEntity, float f, float g, float h, float i, float j) {
        boolean bl = livingEntity.getFallFlyingTicks() > 4;
        boolean bl2 = livingEntity.isInSwimmingPose();
        this.head.yaw = i * (float) (Math.PI / 180.0);
        if (bl) {
            this.head.pitch = (float) (-Math.PI / 4);
        } else if (this.leaningPitch > 0.0F) {
            if (bl2) {
                this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, (float) (-Math.PI / 4));
            } else {
                this.head.pitch = this.lerpAngle(this.leaningPitch, this.head.pitch, j * (float) (Math.PI / 180.0));
            }
        } else {
            this.head.pitch = j * (float) (Math.PI / 180.0);
        }

        this.body.yaw = 0.0F;
        this.rightArm.pivotZ = 0.0F;
        this.rightArm.pivotX = -5.0F;
        this.leftArm.pivotZ = 0.0F;
        this.leftArm.pivotX = 5.0F;
        float k = 1.0F;
        if (bl) {
            k = (float) livingEntity.getVelocity().lengthSquared();
            k /= 0.2F;
            k *= k * k;
        }

        if (k < 1.0F) {
            k = 1.0F;
        }

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
        if (this.riding) {
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

        this.animateArms(livingEntity, h);

        this.hat.copyTransform(this.head);
    }
}
