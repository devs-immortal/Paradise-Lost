package com.aether.client.model.entity;

import com.aether.entities.passive.PhygEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class PhygWingModel extends EntityModel<PhygEntity> {

    private final ModelPart leftWingInner = new ModelPart(this, 0, 0);
    private final ModelPart leftWingOuter = new ModelPart(this, 20, 0);
    private final ModelPart rightWingInner = new ModelPart(this, 0, 0);
    private final ModelPart rightWingOuter = new ModelPart(this, 40, 0);
    private PhygEntity phyg;

    public PhygWingModel() {
        this.leftWingInner.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.leftWingOuter.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingInner.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.yaw = (float) Math.PI;
    }

    @Override
    public void animateModel(PhygEntity entity, float f, float g, float h) {
        phyg = entity;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        float wingBend;
        float x;
        float y;
        float z;
        float x2;
        float y2;
        if (phyg.isBaby()) {
            wingBend = -((float) Math.acos(phyg.wingFold));
            matrices.scale(1.0F / 2.0F, 1.0F / 2.0F, 1.0F / 2.0F);
            matrices.translate(0.0F, 24.0F/* * scale*/, 0.0F);
            x = -((float) Math.acos(phyg.wingFold));
            y = 32.0F * phyg.wingFold / 4.0F;
            z = -32.0F * (float) Math.sqrt(1.0F - phyg.wingFold * phyg.wingFold) / 4.0F;
            x2 = 0.0F;
            y2 = y * (float) Math.cos(phyg.wingAngle) - z * (float) Math.sin(phyg.wingAngle);
            float y21 = y * (float) Math.sin(phyg.wingAngle) + z * (float) Math.cos(phyg.wingAngle);
            this.leftWingInner.setPivot(4.0F + y2, y21 + 12.0F, x2);
            this.rightWingInner.setPivot(-4.0F - y2, y21 + 12.0F, x2);
            y *= 3.0F;
            y2 = y * (float) Math.cos(phyg.wingAngle) - z * (float) Math.sin(phyg.wingAngle);
            y21 = y * (float) Math.sin(phyg.wingAngle) + z * (float) Math.cos(phyg.wingAngle);
            this.leftWingOuter.setPivot(4.0F + y2, y21 + 12.0F, x2);
            this.rightWingOuter.setPivot(-4.0F - y2, y21 + 12.0F, x2);
            this.leftWingInner.roll = phyg.wingAngle + wingBend + ((float) Math.PI / 2F);
            this.leftWingOuter.roll = phyg.wingAngle - wingBend + ((float) Math.PI / 2F);
            this.rightWingInner.roll = -(phyg.wingAngle + wingBend - ((float) Math.PI / 2F));
            this.rightWingOuter.roll = -(phyg.wingAngle - wingBend + ((float) Math.PI / 2F));
            this.leftWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.leftWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.rightWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.rightWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        } else {
            wingBend = -((float) Math.acos(phyg.wingFold));
            x = 32.0F * phyg.wingFold / 4.0F;
            y = -32.0F * (float) Math.sqrt(1.0F - phyg.wingFold * phyg.wingFold) / 4.0F;
            z = 0.0F;
            x2 = x * (float) Math.cos(phyg.wingAngle) - y * (float) Math.sin(phyg.wingAngle);
            y2 = x * (float) Math.sin(phyg.wingAngle) + y * (float) Math.cos(phyg.wingAngle);
            this.leftWingInner.setPivot(4.0F + x2, y2 + 12.0F, z);
            this.rightWingInner.setPivot(-4.0F - x2, y2 + 12.0F, z);
            x *= 3.0F;
            x2 = x * (float) Math.cos(phyg.wingAngle) - y * (float) Math.sin(phyg.wingAngle);
            y2 = x * (float) Math.sin(phyg.wingAngle) + y * (float) Math.cos(phyg.wingAngle);
            this.leftWingOuter.setPivot(4.0F + x2, y2 + 12.0F, z);
            this.rightWingOuter.setPivot(-4.0F - x2, y2 + 12.0F, z);
            this.leftWingInner.roll = phyg.wingAngle + wingBend + ((float) Math.PI / 2F);
            this.leftWingOuter.roll = phyg.wingAngle - wingBend + ((float) Math.PI / 2F);
            this.rightWingInner.roll = -(phyg.wingAngle + wingBend - ((float) Math.PI / 2F));
            this.rightWingOuter.roll = -(phyg.wingAngle - wingBend + ((float) Math.PI / 2F));
            this.leftWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.leftWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.rightWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.rightWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void setAngles(PhygEntity phyg, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.phyg = phyg;
    }

}
