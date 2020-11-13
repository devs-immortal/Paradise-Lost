package com.aether.client.model.entity;

import com.aether.entities.passive.AerbunnyEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class AerbunnyModel extends EntityModel<AerbunnyEntity> {

    public ModelPart a;
    public ModelPart b;
    public ModelPart b2;
    public ModelPart b3;
    public ModelPart e1;
    public ModelPart e2;
    public ModelPart ff1;
    public ModelPart ff2;
    public ModelPart g;
    public ModelPart g2;
    public ModelPart h;
    public ModelPart h2;
    public float puffiness;

    private AerbunnyEntity e;

    public AerbunnyModel() {
        byte byte0 = 16;
        this.a = new ModelPart(this, 0, 0);
        this.a.addCuboid(-2.0F, -1.0F, -4.0F, 4, 4, 6, 0.0F);
        this.a.setPivot(0.0F, (float) (-1 + byte0), -4.0F);
        this.g = new ModelPart(this, 14, 0);
        this.g.addCuboid(-2.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g.setPivot(0.0F, (float) (-1 + byte0), -4.0F);
        this.g2 = new ModelPart(this, 14, 0);
        this.g2.addCuboid(1.0F, -5.0F, -3.0F, 1, 4, 2, 0.0F);
        this.g2.setPivot(0.0F, (float) (-1 + byte0), -4.0F);
        this.h = new ModelPart(this, 20, 0);
        this.h.addCuboid(-4.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h.setPivot(0.0F, (float) (-1 + byte0), -4.0F);
        this.h2 = new ModelPart(this, 20, 0);
        this.h2.addCuboid(2.0F, 0.0F, -3.0F, 2, 3, 2, 0.0F);
        this.h2.setPivot(0.0F, (float) (-1 + byte0), -4.0F);
        this.b = new ModelPart(this, 0, 10);
        this.b.addCuboid(-3.0F, -4.0F, -3.0F, 6, 8, 6, 0.0F);
        this.b.setPivot(0.0F, byte0, 0.0F);
        this.b2 = new ModelPart(this, 0, 24);
        this.b2.addCuboid(-2.0F, 4.0F, -2.0F, 4, 3, 4, 0.0F);
        this.b2.setPivot(0.0F, byte0, 0.0F);
        this.b3 = new ModelPart(this, 29, 0);
        this.b3.addCuboid(-3.5F, -3.5F, -3.5F, 7, 7, 7, 0.0F);
        this.b3.setPivot(0.0F, 0.0F, 0.0F);
        this.e1 = new ModelPart(this, 24, 16);
        this.e1.addCuboid(-2.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e1.setPivot(3.0F, (float) (3 + byte0), -3.0F);
        this.e2 = new ModelPart(this, 24, 16);
        this.e2.addCuboid(0.0F, 0.0F, -1.0F, 2, 2, 2);
        this.e2.setPivot(-3.0F, (float) (3 + byte0), -3.0F);
        this.ff1 = new ModelPart(this, 16, 24);
        this.ff1.addCuboid(-2.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff1.setPivot(3.0F, (float) (3 + byte0), 4.0F);
        this.ff2 = new ModelPart(this, 16, 24);
        this.ff2.addCuboid(0.0F, 0.0F, -4.0F, 2, 2, 4);
        this.ff2.setPivot(-3.0F, (float) (3 + byte0), 4.0F);
    }

    @Override
    public void animateModel(AerbunnyEntity entity, float f, float g, float h) {
        e = entity;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.puffiness = (float) e.getPuffiness() / 10.0F;

        float a;
        if (this.child) {
            a = 2.0F;
            matrices.push();
            matrices.translate(0.0F, 0.0f, 0.0f);
            this.a.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.g.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.g2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.h.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.h2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
            matrices.scale(1.0F / a, 1.0F / a, 1.0F / a);
            matrices.translate(0.0F, 18.0F/* * f5*/, 0.0F);
            this.b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.b2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.e1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.e2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.ff1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.ff2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            float a1 = 1.0F + this.puffiness * 0.5F;
            matrices.translate(0.0F, 1.0F, 0.0F);
            matrices.scale(a1, a1, a1);
            this.b3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        } else {
            this.a.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.g.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.g2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.h.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.h2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.b.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.b2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.push();
            a = 1.0F + this.puffiness * 0.5F;
            matrices.translate(0.0F, 1.0F, 0.0F);
            matrices.scale(a, a, a);
            this.b3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            matrices.pop();
            this.e1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.e2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.ff1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.ff2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }
    }

    @Override
    public void setAngles(AerbunnyEntity e, float f, float f1, float f2, float f3, float f4) {
        this.e = e;
        this.a.pitch = -(f4 / (180F / (float) Math.PI));
        this.a.yaw = f3 / (180F / (float) Math.PI);
        this.g.pitch = this.a.pitch;
        this.g.yaw = this.a.yaw;
        this.g2.pitch = this.a.pitch;
        this.g2.yaw = this.a.yaw;
        this.h.pitch = this.a.pitch;
        this.h.yaw = this.a.yaw;
        this.h2.pitch = this.a.pitch;
        this.h2.yaw = this.a.yaw;
        this.b.pitch = ((float) Math.PI / 2F);
        this.b2.pitch = ((float) Math.PI / 2F);
        this.b3.pitch = ((float) Math.PI / 2F);
        this.e1.pitch = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
        this.ff1.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.2F * f1;
        this.e2.pitch = MathHelper.cos(f * 0.6662F) * 1.0F * f1;
        this.ff2.pitch = MathHelper.cos(f * 0.6662F + (float) Math.PI) * 1.2F * f1;
    }

}
