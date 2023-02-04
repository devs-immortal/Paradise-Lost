package net.id.paradiselost.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.entities.hostile.HellenroseEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class ParadiseLostPlantModel extends EntityModel<HellenroseEntity> {
    private final ModelPart root;
    private final ModelPart[] petals;
    private final ModelPart[] leafs;

    public ParadiseLostPlantModel(ModelPart modelRoot) {
        root = modelRoot.getChild("root");
        petals = new ModelPart[5];
        leafs = new ModelPart[5];
        for (int i = 0; i < 5; i++) {
            petals[i] = root.getChild("petal_" + (i + 1));
            leafs[i] = root.getChild("leaf_" + (i + 1));
        }
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelRoot = modelData.getRoot();

        ModelPartBuilder bodyBuilder = ModelPartBuilder.create();
        bodyBuilder.uv(40, 23).cuboid(-1.0F, -2.0F, -1.0F, 2.0F, 3.0F, 2.0F);
        bodyBuilder.uv(8, 0).cuboid(-5.0F, -0.25F, -5.0F, 10.0F, 0.0F, 10.0F);
        ModelPartData root = modelRoot.addChild("root", bodyBuilder, ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartBuilder petal1Builder = ModelPartBuilder.create();
        petal1Builder.uv(0, 25).cuboid(0.0F, -1.0F, -3.0F, 8.0F, 0.01F, 6.0F);
        root.addChild("petal_1", petal1Builder, ModelTransform.of(0.0F, 0.0F, 0.0F, 0, 3.1416F - 0.3142F, 0.2618F));

        ModelPartBuilder petal2Builder = ModelPartBuilder.create();
        petal2Builder.uv(0, 25).cuboid(0.0F, -1.0F, -3.0F, 8.0F, 0.01F, 6.0F);
        root.addChild("petal_2", petal2Builder, ModelTransform.of(0.0F, 0.0F, 0.0F, 0, 0.3142F, -0.2618F));

        ModelPartBuilder petal3Builder = ModelPartBuilder.create();
        petal3Builder.uv(0, 16).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 0.01F, 8.0F);
        root.addChild("petal_3", petal3Builder, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, -0.6283F, 0.0F));

        ModelPartBuilder petal4Builder = ModelPartBuilder.create();
        petal4Builder.uv(0, 16).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 0.01F, 8.0F);
        root.addChild("petal_4", petal4Builder, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F, 3.1416F, 0));

        ModelPartBuilder petal5Builder = ModelPartBuilder.create();
        petal5Builder.uv(0, 16).cuboid(-3.0F, -1.0F, 0.0F, 6.0F, 0.01F, 8.0F);
        root.addChild("petal_5", petal5Builder, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.2618F - 3.1416F, -0.6283F + 3.1416F, 3.1416F));

        ModelPartBuilder leaf1_r1 = ModelPartBuilder.create();
        leaf1_r1.uv(0, 11).cuboid(0.0F, -0.5F, -2.0F, 7.0F, 0.01F, 4.0F);
        root.addChild("leaf_1", leaf1_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0, 3.1416F - 0.9425F, 0.1745F));

        ModelPartBuilder leaf2_r1 = ModelPartBuilder.create();
        leaf2_r1.uv(0, 3).cuboid(-2.0F, -0.5F, 0.0F, 4.0F, 0.01F, 7.0F);
        root.addChild("leaf_2", leaf2_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        ModelPartBuilder leaf3_r1 = ModelPartBuilder.create();
        leaf3_r1.uv(0, 11).cuboid(0.0F, -0.5F, -2.0F, 7.0F, 0.01F, 4.0F);
        root.addChild("leaf_3", leaf3_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, -0.3142F, -0.1745F));

        ModelPartBuilder leaf5_r1 = ModelPartBuilder.create();
        leaf5_r1.uv(0, 11).cuboid(0.0F, -0.5F, -2.0F, 7.0F, 0.01F, 4.0F);
        root.addChild("leaf_5", leaf5_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0, 3.1416F + 0.3142F, 0.1745F));

        ModelPartBuilder leaf4_r1 = ModelPartBuilder.create();
        leaf4_r1.uv(0, 3).cuboid(-2.0F, -0.5F, 0.0F, 4.0F, 0.01F, 7.0F);
        root.addChild("leaf_4", leaf4_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 3.1416F - 0.6283F, 0));

        ModelPartBuilder pistil = ModelPartBuilder.create();

        pistil.uv(28, 25).cuboid(-2.0F, -2.5F, -2.0F, 4.0F, 2.0F, 4.0F);
        root.addChild("pistil", pistil, ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartBuilder stamenBuilder = ModelPartBuilder.create();
        ModelPartData stamen = root.addChild("stamen", stamenBuilder, ModelTransform.pivot(0.0F, 0.0F, 0.0F));

        ModelPartBuilder stamen_w_r1 = ModelPartBuilder.create();
        stamen_w_r1.uv(38, 13).cuboid(1.0F, -7.0F, -2.0F, 0.0F, 5.0F, 4.0F);
        stamen.addChild("stamen_w_r1", stamen_w_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.1745F));

        ModelPartBuilder stamen_s_r1 = ModelPartBuilder.create();
        stamen_s_r1.uv(25, 17).cuboid(-2.0F, -7.0F, -1.0F, 4.0F, 5.0F, 0.0F);
        stamen.addChild("stamen_s_r1", stamen_s_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, -2.9671F, 0.0F, 3.1416F));

        ModelPartBuilder stamen_e_r1 = ModelPartBuilder.create();
        stamen_e_r1.uv(23, 13).cuboid(-1.0F, -7.0F, -2.0F, 0.0F, 5.0F, 4.0F);
        stamen.addChild("stamen_e_r1", stamen_e_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, -0.1745F));

        ModelPartBuilder stamen_n_r1 = ModelPartBuilder.create();
        stamen_n_r1.uv(25, 17).cuboid(-2.0F, -7.0F, -1.0F, 4.0F, 5.0F, 0.0F);
        stamen.addChild("stamen_n_r1", stamen_n_r1, ModelTransform.of(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        return TexturedModelData.of(modelData, 48, 32);
    }

    @Override
    public void setAngles(HellenroseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        setRotationAngle(petals[0], 0, 2.8274F, 0.2618F);
        setRotationAngle(petals[1], 0, 0.3142F, -0.2618F);
        setRotationAngle(petals[2], 0.2618F, -0.6283F, 0.0F);
        setRotationAngle(petals[3], 0.2618F, 3.1416F, 0);
        setRotationAngle(petals[4], 0.2618F, 0.6283F, 0);

        for (ModelPart petal : petals) {
            petal.pitch *= 1 + 0.3F * MathHelper.sin(ageInTicks / 20);
            petal.roll *= 1 + 0.3F * MathHelper.sin(ageInTicks / 20);
        }

        //        for (ModelPart petal : new ModelPart[] { this.petals[0], this.petals[1] }) {
        //            petal.pitch = MathHelper.sin(petal.yaw) * (-0.2618F + 0.2F * MathHelper.sin(ageInTicks / 20));
        //            petal.roll = MathHelper.cos(petal.yaw) * (-0.2618F + 0.2F * MathHelper.sin(ageInTicks / 20));
        //        }
        //        for (ModelPart petal : new ModelPart[] { this.petals[2], this.petals[3], this.petals[4] }) {
        //            petal.pitch = MathHelper.cos(petal.yaw) * (-0.2618F + 0.2F * MathHelper.sin(ageInTicks / 20));
        //            petal.roll = MathHelper.sin(petal.yaw) * (-0.2618F + 0.2F * MathHelper.sin(ageInTicks / 20));
        //        }
    }

    @Override
    public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        root.render(matrixStack, buffer, packedLight, packedOverlay);
    }

    public void setRotationAngle(ModelPart bone, float x, float y, float z) {
        bone.pitch = x;
        bone.yaw = y;
        bone.roll = z;
    }
}
