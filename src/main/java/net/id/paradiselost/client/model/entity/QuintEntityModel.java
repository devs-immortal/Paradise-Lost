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
    private final ModelPart layer;

    public QuintEntityModel(ModelPart root) {
        this.center = root.getChild("center");
        this.layer = root.getChild("layer");
    }

    @Override
    public void setAngles(QuintEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        this.center.yaw = animationProgress / 2.0F;
        this.layer.yaw = -animationProgress;
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        modelPartData.addChild("center", ModelPartBuilder.create().uv(0, 0).cuboid(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 19.0F, 0.0F));
        modelPartData.addChild("layer", ModelPartBuilder.create().uv(0, 16).cuboid(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 19.0F, 0.0F));

        return TexturedModelData.of(modelData, 32, 32);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        this.center.render(matrices, vertices, 15728640, overlay, color);
        this.layer.render(matrices, vertices, 15728640, overlay, color);
    }
}
