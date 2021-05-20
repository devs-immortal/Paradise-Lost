package com.aether.client.model.entity;

import com.aether.entities.hostile.ChestMimicEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class ChestMimicModel extends EntityModel<ChestMimicEntity> {

    public final ModelPart box, boxLid, leftLeg, rightLeg;

    public ChestMimicModel() {
        this.box = new ModelPart(this, 0, 0);
        this.box.addCuboid(-8F, 0F, -8F, 16, 10, 16);
        this.box.setPivot(0F, -24F, 0F);

        this.boxLid = new ModelPart(this, 16, 10);
        this.boxLid.addCuboid(0F, 0F, 0F, 16, 6, 16);
        this.boxLid.setPivot(-8F, -24F, 8F);

        this.leftLeg = new ModelPart(this, 0, 0);
        this.leftLeg.addCuboid(-3F, 0F, -3F, 6, 15, 6);
        this.leftLeg.setPivot(-4F, -15F, 0F);

        this.rightLeg = new ModelPart(this, 0, 0);
        this.rightLeg.addCuboid(-3F, 0F, -3F, 6, 15, 6);
        this.rightLeg.setPivot(4F, -15F, 0F);

    }

    @Override
    public void setAngles(ChestMimicEntity entityIn, float f, float f1, float f2, float f3, float f4) {
        this.boxLid.pivotX = 3.14159265F - entityIn.mouth;
        this.rightLeg.pivotX = entityIn.legs;
        this.leftLeg.pivotX = -entityIn.legs;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        box.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        boxLid.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        leftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}