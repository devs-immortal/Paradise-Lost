// Made with Model Converter by Globox_Z
// Generate all required imports
// Made with Blockbench 3.8.3
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
package net.id.aether.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.passive.AerwhaleEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class AerwhaleModel extends EntityModel<AerwhaleEntity> {
    final float pi = 3.1415927F;
    private final ModelPart aerwhale_head;
    private final ModelPart aerwhale_body1;
    private final ModelPart aerwhale_body2;
    private final ModelPart l_tail;
    private final ModelPart r_tail;
    private final ModelPart l_fin;
    private final ModelPart r_fin;

    public AerwhaleModel(ModelPart root) {
        this.aerwhale_head = root.getChild("aerwhale_head");
        this.r_fin = this.aerwhale_head.getChild("r_fin");
        this.l_fin = this.aerwhale_head.getChild("l_fin");
        this.aerwhale_body1 = this.aerwhale_head.getChild("aerwhale_body1");
        this.aerwhale_body2 = this.aerwhale_body1.getChild("aerwhale_body2");
        this.r_tail = this.aerwhale_body2.getChild("r_tail");
        this.l_tail = this.aerwhale_body2.getChild("l_tail");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        ModelPartData modelPartData1 = modelPartData.addChild("aerwhale_head", ModelPartBuilder.create().uv(60, 0).cuboid(-10.5F, -18.0F, -29.0F, 21.0F, 18.0F, 30.0F), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        ModelPartData modelPartData2 = modelPartData1.addChild("aerwhale_body1", ModelPartBuilder.create().uv(0, 0).cuboid(-7.5F, -7.5F, 0.0F, 15.0F, 15.0F, 15.0F), ModelTransform.pivot(0.0F, -7.5F, -1.0F));
        ModelPartData modelPartData3 = modelPartData2.addChild("aerwhale_body2", ModelPartBuilder.create().uv(0, 30).cuboid(-4.5F, -4.5F, 0.0F, 9.0F, 9.0F, 12.0F), ModelTransform.pivot(0.0F, 0.0F, 13.0F));
        modelPartData3.addChild("l_tail", ModelPartBuilder.create().uv(0, 51).cuboid(0.0F, 0.0F, 0.0F, 24.0F, 3.0F, 12.0F), ModelTransform.pivot(4.5F, 0.5F, 2.0F));
        modelPartData3.addChild("r_tail", ModelPartBuilder.create().uv(0, 66).cuboid(0.0F, 0.0F, -12.0F, 24.0F, 3.0F, 12.0F), ModelTransform.of(-4.5F, 0.5F, 2.0F, 0.0F, 3.1416F, 0.0F));
        modelPartData1.addChild("l_fin", ModelPartBuilder.create().uv(72, 48).cuboid(0.0F, -2.0F, 0.0F, 12.0F, 3.0F, 6.0F), ModelTransform.pivot(10.5F, -2.0F, -13.0F));
        modelPartData1.addChild("r_fin", ModelPartBuilder.create().uv(72, 57).cuboid(0.0F, -2.0F, -6.0F, 12.0F, 3.0F, 6.0F), ModelTransform.of(-10.5F, -2.0F, -12.0F, 0.0F, 3.1416F, 0.0F));

        return TexturedModelData.of(modelData, 192, 96);
    }

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
}
