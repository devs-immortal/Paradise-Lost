package com.aether.client.model.entity;

import com.aether.entities.hostile.CockatriceEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class CockatriceModel extends EntityModel<CockatriceEntity> {

    public final ModelPart head, body;
    public final ModelPart legs, legs2;
    public final ModelPart wings, wings2;
    public final ModelPart jaw, neck;
    public final ModelPart feather1, feather2, feather3;

    public CockatriceModel(ModelPart root) {
        this.head = root.getChild("head");
        this.jaw = root.getChild("jaw");
        this.body = root.getChild("body");
        this.legs = root.getChild("legs");
        this.legs2 = root.getChild("legs2");
        this.wings = root.getChild("wings");
        this.wings2 = root.getChild("wings2");
        this.neck = root.getChild("neck");
        this.feather1 = root.getChild("feather1");
        this.feather2 = root.getChild("feather2");
        this.feather3 = root.getChild("feather3");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("head", ModelPartBuilder.create().uv(0, 13).cuboid(-2.0F, -4.0F, -6.0F, 4, 4, 8), ModelTransform.pivot(0.0F, (float) (-8 + 16), -4.0F));
        modelPartData.addChild("jaw", ModelPartBuilder.create().uv(24, 13).cuboid(-2.0F, -1.0F, -6.0F, 4, 1, 8), ModelTransform.pivot(0.0F, (float) (-8 + 16), -4.0F));
        modelPartData.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -3.0F, 0.0F, 6, 8, 5), ModelTransform.pivot(0.0F, (float) (16), 0.0F));
        modelPartData.addChild("legs", ModelPartBuilder.create().uv(22, 0).cuboid(-1.0F, -1.0F, -1.0F, 2, 9, 2), ModelTransform.pivot(-2.0F, (float) (16), 1.0F));
        modelPartData.addChild("legs2", ModelPartBuilder.create().uv(22, 0).cuboid(-1.0F, -1.0F, -1.0F, 2, 9, 2), ModelTransform.pivot(2.0F, (float) (16), 1.0F));
        modelPartData.addChild("wings", ModelPartBuilder.create().uv(52, 0).cuboid(-1.0F, -0.0F, -1.0F, 1, 8, 4), ModelTransform.pivot(-3.0F, (float) (16), 2.0F));
        modelPartData.addChild("wings2", ModelPartBuilder.create().uv(52, 0).cuboid(0.0F, -0.0F, -1.0F, 1, 8, 4), ModelTransform.pivot(3.0F, (float) (-4 + 16), 0.0F));
        modelPartData.addChild("neck", ModelPartBuilder.create().uv(44, 0).cuboid(-1.0F, -6.0F, -1.0F, 2, 6, 2), ModelTransform.pivot(0.0F, (float) (-2 + 16), -4.0F));
        modelPartData.addChild("feather1", ModelPartBuilder.create().uv(30, 0).cuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5), ModelTransform.pivot(0.0F, (float) (1 + 16) + 0.5F, 1.0F));
        modelPartData.addChild("feather2", ModelPartBuilder.create().uv(30, 0).cuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5), ModelTransform.pivot(0.0F, (float) (1 + 16) + 0.5F, 1.0F));
        modelPartData.addChild("feather3", ModelPartBuilder.create().uv(30, 0).cuboid(-1.0F, -5.0F, 5.0F, 2, 1, 5), ModelTransform.pivot(0.0F, (float) (1 + 16) + 0.5F, 1.0F));

        return TexturedModelData.of(modelData,64,64);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        this.head.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.jaw.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.body.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.legs.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.legs2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.wings.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.wings2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.neck.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather1.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather2.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        this.feather3.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }

    @Override
    public void setAngles(CockatriceEntity cockatrice, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.pitch = headPitch / 57.29578F;
        this.head.yaw = netHeadYaw / 57.29578F;
        this.jaw.pitch = this.head.pitch;
        this.jaw.yaw = this.head.yaw;
        this.body.pitch = 1.570796F;
        this.legs.pitch = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
        this.legs2.pitch = MathHelper.cos((float) (limbSwing * 0.6662F + Math.PI)) * 1.4F * limbSwingAmount;

        if (ageInTicks > 0.001F) {
            this.wings.pivotZ = -1F;
            this.wings2.pivotZ = -1F;
            this.wings.pivotY = 12F;
            this.wings2.pivotY = 12F;
            this.wings.pitch = 0.0F;
            this.wings2.pitch = 0.0F;
            this.wings.roll = ageInTicks;
            this.wings2.roll = -ageInTicks;
            this.legs.pitch = 0.6F;
            this.legs2.pitch = 0.6F;
        } else {
            this.wings.pivotZ = -3F;
            this.wings2.pivotZ = -3F;
            this.wings.pivotY = 14F;
            this.wings2.pivotY = 14F;
            this.wings.pitch = (float) (Math.PI / 2.0F);
            this.wings2.pitch = (float) (Math.PI / 2.0F);
            this.wings.roll = 0.0F;
            this.wings2.roll = 0.0F;
        }

        this.feather1.yaw = -0.375F;
        this.feather2.yaw = 0.0F;
        this.feather3.yaw = 0.375F;
        this.feather1.pitch = 0.25F;
        this.feather2.pitch = 0.25F;
        this.feather3.pitch = 0.25F;
        this.neck.pitch = 0.0F;
        this.neck.yaw = head.yaw;
        this.jaw.pitch += 0.35F;
    }
}
