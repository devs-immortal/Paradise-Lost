package net.id.paradiselost.client.model.entity;

import net.id.paradiselost.entities.hostile.QuintEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

public class QuintEntityModel extends EntityModel<QuintEntity> {

    private final ModelPart center;

    public QuintEntityModel(ModelPart root) {
        this.center = root.getChild("center");
    }

    @Override
    public void setAngles(QuintEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.center.yaw = animationProgress / 3.0F;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("center", ModelPartBuilder.create().uv(0, 0).cuboid(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));
        return TexturedModelData.of(modelData, 32, 16);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.center.render(matrices, vertices, light, overlay, color);
    }
}
