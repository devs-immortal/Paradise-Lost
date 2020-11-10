package com.aether.client.model;

import com.aether.entities.hostile.AechorPlantEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class AechorPlantModel extends EntityModel<AechorPlantEntity> {

    public float sinage;
    public float sinage2;
    public float size;
    private final ModelPart[] petal;
    private final ModelPart[] leaf;
    private final ModelPart[] stamen;
    private final ModelPart[] stamen2;
    private final ModelPart[] thorn;
    private final ModelPart stem;
    private final ModelPart head;

    public AechorPlantModel() {
        this(0.0F);
    }

    public AechorPlantModel(float modelSize) {
        this(modelSize, 0.0F);
    }

    public AechorPlantModel(float modelSize, float rotationPointY) {
        this.size = 1.0F;
        this.petal = new ModelPart[10];
        this.leaf = new ModelPart[10];
        this.stamen = new ModelPart[3];
        this.stamen2 = new ModelPart[3];
        this.thorn = new ModelPart[4];

        for (int i = 0; i < 10; i++) {
            this.petal[i] = new ModelPart(this, 0, 0);

            if (i % 2 == 0) {
                this.petal[i] = new ModelPart(this, 29, 3);
                this.petal[i].addCuboid(-4F, -1F, -12F, 8, 1, 9, modelSize - 0.25F);
            } else {
                this.petal[i].addCuboid(-4F, -1F, -13F, 8, 1, 10, modelSize - 0.125F);
            }
            this.petal[i].setPivot(0.0F, 1.0F + rotationPointY, 0.0F);

            this.leaf[i] = new ModelPart(this, 38, 13);
            this.leaf[i].addCuboid(-2F, -1F, -9.5F, 4, 1, 8, modelSize - 0.15F);
            this.leaf[i].setPivot(0.0F, 1.0F + rotationPointY, 0.0F);
        }

        for (int i = 0; i < 3; i++) {
            this.stamen[i] = new ModelPart(this, 36, 13);
            this.stamen[i].addCuboid(0F, -9F, -1.5F, 1, 6, 1, modelSize - 0.25F);
            this.stamen[i].setPivot(0.0F, 1.0F + rotationPointY, 0.0F);
        }

        for (int i = 0; i < 3; i++) {
            this.stamen2[i] = new ModelPart(this, 32, 15);
            this.stamen2[i].addCuboid(0F, -10F, -1.5F, 1, 1, 1, modelSize + 0.125F);
            this.stamen2[i].setPivot(0.0F, 1.0F + rotationPointY, 0.0F);
        }

        this.head = new ModelPart(this, 0, 12);
        this.head.addCuboid(-3F, -3F, -3F, 6, 2, 6, modelSize + 0.75F);
        this.head.setPivot(0.0F, 1.0F + rotationPointY, 0.0F);
        this.stem = new ModelPart(this, 24, 13);
        this.stem.addCuboid(-1F, 0F, -1F, 2, 6, 2, modelSize);
        this.stem.setPivot(0.0F, 1.0F + rotationPointY, 0.0F);

        for (int i = 0; i < 4; i++) {
            this.thorn[i] = new ModelPart(this, 32, 13);
            this.thorn[i].setPivot(0.0F, 1.0F + rotationPointY, 0.0F);
        }

        this.thorn[0].addCuboid(-1.75F, 1.25F, -1F, 1, 1, 1, modelSize - 0.25F);
        this.thorn[1].addCuboid(-1F, 2.25F, 0.75F, 1, 1, 1, modelSize - 0.25F);
        this.thorn[2].addCuboid(0.75F, 1.25F, 0F, 1, 1, 1, modelSize - 0.25F);
        this.thorn[3].addCuboid(0F, 2.25F, -1.75F, 1, 1, 1, modelSize - 0.25F);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        matrices.translate(0.0F, 1.2F, 0.0F);
        matrices.scale(this.size, this.size, this.size);

        for (int i = 0; i < 10; i++) {
            this.petal[i].render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.leaf[i].render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }

        for (int i = 0; i < 3; i++) {
            this.stamen[i].render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
            this.stamen2[i].render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }

        this.head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.stem.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);

        for (int i = 0; i < 4; i++) {
            this.thorn[i].render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        }

        matrices.pop();
    }

    @Override
    public void setAngles(AechorPlantEntity aechorPlant, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.pitch = 0.0F;
        this.head.yaw = headPitch / 57.29578F;
        float boff = this.sinage2;
        this.stem.yaw = head.yaw;
        this.stem.pivotY = boff * 0.5F;

        for (int i = 0; i < 10; i++) {
            if (i < 3) {
                this.stamen[i].pitch = 0.2F + ((float) i / 15F);
                this.stamen[i].yaw = head.yaw + 0.1F;
                this.stamen[i].yaw += ((Math.PI * 2) / (float) 3) * (float) i;
                this.stamen[i].pitch += sinage * 0.4F;
                this.stamen2[i].pitch = 0.2F + ((float) i / 15F);
                this.stamen2[i].yaw = head.yaw + 0.1F;
                this.stamen2[i].yaw += ((Math.PI * 2) / (float) 3) * (float) i;
                this.stamen2[i].pitch += sinage * 0.4F;
                this.stamen[i].pivotY = boff + ((sinage) * 2F);
                this.stamen2[i].pivotY = boff + ((sinage) * 2F);
            }

            if (i < 4) {
                this.thorn[i].yaw = head.yaw;
                this.thorn[i].pivotY = boff * 0.5F;
            }

            this.petal[i].pitch = ((i % 2 == 0) ? -0.25F : -0.4125F);
            this.petal[i].pitch += sinage;
            this.petal[i].yaw = head.yaw;
            this.petal[i].yaw += ((Math.PI * 2) / (float) 10) * (float) i;
            this.leaf[i].pitch = ((i % 2 == 0) ? 0.1F : 0.2F);
            this.leaf[i].pitch += sinage * 0.75F;
            this.leaf[i].yaw = (float) (head.yaw + ((Math.PI * 2) / (float) 10 / 2F));
            this.leaf[i].yaw += ((Math.PI * 2) / (float) 10) * (float) i;
            this.petal[i].pivotY = boff;
            this.leaf[i].pivotY = boff;
        }

        this.head.pivotY = boff + ((sinage) * 2F);
    }
}
