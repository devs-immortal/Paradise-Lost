package com.aether.client.model;

import com.aether.entities.passive.FlyingCowEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class FlyingCowWingModel extends EntityModel<FlyingCowEntity> {

    private final ModelPart leftWingInner = new ModelPart(this, 0, 0);
    private final ModelPart leftWingOuter = new ModelPart(this, 20, 0);
    private final ModelPart rightWingInner = new ModelPart(this, 0, 0);
    private final ModelPart rightWingOuter = new ModelPart(this, 40, 0);
    private FlyingCowEntity entityIn;

    public FlyingCowWingModel() {
        this.leftWingInner.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.leftWingOuter.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingInner.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);
        this.rightWingOuter.addCuboid(-1.0F, -8.0F, -4.0F, 2, 16, 8, 0.0F);

        this.rightWingOuter.yaw = (float) Math.PI;
    }

    @Override
    public void setAngles(FlyingCowEntity entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        entityIn = entity;
    }

    @Override
    public void animateModel(FlyingCowEntity entity, float f, float g, float h) {
        entityIn = entity;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();

        matrices.translate(0.0F, -10.0F/* * scale*/, 0.0F);

        float wingBend = -((float) Math.acos(entityIn.wingFold));

        float x = 32.0F * entityIn.wingFold / 4.0F;
        float y = -32.0F * (float) Math.sqrt(1.0F - entityIn.wingFold * entityIn.wingFold) / 4.0F;

        float x2 = x * (float) Math.cos(entityIn.wingAngle) - y * (float) Math.sin(entityIn.wingAngle);
        float y2 = x * (float) Math.sin(entityIn.wingAngle) + y * (float) Math.cos(entityIn.wingAngle);

        this.leftWingInner.setPivot(4.0F + x2, y2 + 12.0F, 0.0F);
        this.rightWingInner.setPivot(-4.0F - x2, y2 + 12.0F, 0.0F);

        x *= 3.0F;
        x2 = x * (float) Math.cos(entityIn.wingAngle) - y * (float) Math.sin(entityIn.wingAngle);
        y2 = x * (float) Math.sin(entityIn.wingAngle) + y * (float) Math.cos(entityIn.wingAngle);

        this.leftWingOuter.setPivot(4.0F + x2, y2 + 12.0F, 0.0F);
        this.rightWingOuter.setPivot(-4.0F - x2, y2 + 12.0F, 0.0F);

        this.leftWingInner.roll = entityIn.wingAngle + wingBend + ((float) Math.PI / 2F);
        this.leftWingOuter.roll = entityIn.wingAngle - wingBend + ((float) Math.PI / 2F);
        this.rightWingInner.roll = -(entityIn.wingAngle + wingBend - ((float) Math.PI / 2F));
        this.rightWingOuter.roll = -(entityIn.wingAngle - wingBend + ((float) Math.PI / 2F));

        this.leftWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.leftWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.rightWingOuter.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.rightWingInner.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

        matrices.pop();
    }

}
