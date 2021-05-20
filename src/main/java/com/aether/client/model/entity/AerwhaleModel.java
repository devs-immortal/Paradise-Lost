// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports

package com.aether.client.model.entity;

import com.aether.entities.passive.AerwhaleEntity;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class AerwhaleModel extends EntityModel<AerwhaleEntity> {

    private final ModelPart aerwhale_head;
    private final ModelPart aerwhale_body1, aerwhale_body2;
    private final ModelPart l_tail, r_tail;
    private final ModelPart l_fin, r_fin;

    public AerwhaleModel() {
        textureWidth = 192;
        textureHeight = 96;

        aerwhale_head = new ModelPart(this);
        aerwhale_head.setPivot(0.0F, 24.0F, 0.0F);
        aerwhale_head.setTextureOffset(60, 0).addCuboid(-10.5F, -18.0F, -29.0F, 21.0F, 18.0F, 30.0F, 0.0F, false);

        aerwhale_body1 = new ModelPart(this);
        aerwhale_body1.setPivot(0.0F, -7.5F, -1.0F);
        aerwhale_head.addChild(aerwhale_body1);
        aerwhale_body1.setTextureOffset(0, 0).addCuboid(-7.5F, -7.5F, 0.0F, 15.0F, 15.0F, 15.0F, 0.0F, false);

        aerwhale_body2 = new ModelPart(this);
        aerwhale_body2.setPivot(0.0F, 0.0F, 13.0F);
        aerwhale_body1.addChild(aerwhale_body2);
        aerwhale_body2.setTextureOffset(0, 30).addCuboid(-4.5F, -4.5F, 0.0F, 9.0F, 9.0F, 12.0F, 0.0F, false);

        l_tail = new ModelPart(this);
        l_tail.setPivot(4.5F, 0.5F, 2.0F);
        aerwhale_body2.addChild(l_tail);
        l_tail.setTextureOffset(0, 51).addCuboid(0.0F, 0.0F, 0.0F, 24.0F, 3.0F, 12.0F, 0.0F, false);

        r_tail = new ModelPart(this);
        r_tail.setPivot(-4.5F, 0.5F, 2.0F);
        aerwhale_body2.addChild(r_tail);
        setRotationAngle(r_tail, 0.0F, 3.1416F, 0.0F);
        r_tail.setTextureOffset(0, 66).addCuboid(0.0F, 0.0F, -12.0F, 24.0F, 3.0F, 12.0F, 0.0F, false);

        l_fin = new ModelPart(this);
        l_fin.setPivot(10.5F, -2.0F, -13.0F);
        aerwhale_head.addChild(l_fin);
        l_fin.setTextureOffset(72, 48).addCuboid(0.0F, -2.0F, 0.0F, 12.0F, 3.0F, 6.0F, 0.0F, false);

        r_fin = new ModelPart(this);
        r_fin.setPivot(-10.5F, -2.0F, -12.0F);
        aerwhale_head.addChild(r_fin);
        setRotationAngle(r_fin, 0.0F, 3.1416F, 0.0F);
        r_fin.setTextureOffset(72, 57).addCuboid(0.0F, -2.0F, -6.0F, 12.0F, 3.0F, 6.0F, 0.0F, false);
    }

    final float pi = 3.1415927F;

    @Override
    public void setAngles(AerwhaleEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float time = ageInTicks / 20;
        //aerwhale_body1.yaw = MathHelper.cos(time)/4;
        //aerwhale_head.pitch = MathHelper.sin(time)/36;
        aerwhale_body1.pitch = MathHelper.cos(time - 1) / 10;
        aerwhale_body2.pitch = MathHelper.cos(time - 2) / 10;
        l_fin.yaw = MathHelper.cos(limbSwing) / 4;
        r_fin.yaw = pi + -MathHelper.cos(limbSwing) / 4;
        l_tail.yaw = MathHelper.cos(limbSwing + 10) / 6;
        r_tail.yaw = pi + -MathHelper.cos(limbSwing + 10) / 6;
        l_tail.pitch = MathHelper.cos(time + 1.5F) / 5;
        r_tail.pitch = MathHelper.cos(time - 1.5F) / 5;

        //previously the render function, render code was moved to a method below
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        aerwhale_head.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}