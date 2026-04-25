package net.id.paradiselost.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.id.paradiselost.entities.passive.PopomEntity;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;

public class PopomEntityModel<T extends PopomEntity> extends AnimalModel<T> {

    public int furSize = 0;

    private final ModelPart body0;
    private final ModelPart body1;
    private final ModelPart body2;
    private final ModelPart body3;
    private final ModelPart head;
    private final ModelPart frleg;
    private final ModelPart flleg;
    private final ModelPart brleg;
    private final ModelPart blleg;
    private final ModelPart mrleg;
    private final ModelPart mlleg;

    public PopomEntityModel(ModelPart root) {
        super(true, 25.0F, 2.0F, 3.0F, 3.0F, 48.0F);
        this.body0 = root.getChild("body0");
        this.body1 = root.getChild("body1");
        this.body2 = root.getChild("body2");
        this.body3 = root.getChild("body3");
        this.head = root.getChild("head");
        this.frleg = root.getChild("frleg");
        this.flleg = root.getChild("flleg");
        this.brleg = root.getChild("brleg");
        this.blleg = root.getChild("blleg");
        this.mrleg = root.getChild("mrleg");
        this.mlleg = root.getChild("mlleg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData root = ModelData.getRoot();

        ModelPartData body0 = root.addChild("body0", ModelPartBuilder.create().uv(0, 78).cuboid(-5.0F, -9.0F, -8.0F, 10.0F, 6.0F, 15.0F, new Dilation(1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body1 = root.addChild("body1", ModelPartBuilder.create().uv(0, 54).cuboid(-5.5F, -11.0F, -8.0F, 11.0F, 8.0F, 16.0F, new Dilation(1.05F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body2 = root.addChild("body2", ModelPartBuilder.create().uv(0, 28).cuboid(-6.0F, -12.0F, -8.0F, 12.0F, 9.0F, 17.0F, new Dilation(1.1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData body3 = root.addChild("body3", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -13.0F, -8.0F, 14.0F, 10.0F, 18.0F, new Dilation(1.15F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        ModelPartData head = root.addChild("head", ModelPartBuilder.create().uv(38, 54).cuboid(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 4.0F)
                .uv(38, 62).cuboid(-3.0F, -5.0F, -5.0F, 6.0F, 3.0F, 3.0F), ModelTransform.pivot(0.0F, 19.0F, -8.0F));

        ModelPartData frleg = root.addChild("frleg", ModelPartBuilder.create().uv(0, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, -6.0F));

        ModelPartData flleg = root.addChild("flleg", ModelPartBuilder.create().uv(0, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, -6.0F));

        ModelPartData brleg = root.addChild("brleg", ModelPartBuilder.create().uv(12, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, 4.0F));

        ModelPartData blleg = root.addChild("blleg", ModelPartBuilder.create().uv(12, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, 4.0F));

        ModelPartData mrleg = root.addChild("mrleg", ModelPartBuilder.create().uv(12, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, -1.0F));

        ModelPartData mlleg = root.addChild("mlleg", ModelPartBuilder.create().uv(12, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, -1.0F));

        return TexturedModelData.of(ModelData, 64, 128);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        super.render(matrices, vertices, light, overlay, color);
        if (this.child) {
            matrices.push();
            float f = 0.5F;
            matrices.scale(f, f, f);
            matrices.translate(0.0F, 1.6F, 0.0F);
            this.getFurs()[1].render(matrices, vertices, light, overlay, color);
            matrices.pop();
        } else {
            this.getFurs()[Math.min(3, furSize)].render(matrices, vertices, light, overlay, color);
        }
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.frleg, this.flleg, this.brleg, this.blleg, this.mrleg, this.mlleg);
    }

    protected ModelPart[] getFurs() {
        return new ModelPart[]{this.body0, this.body1, this.body2, this.body3};
    }

    @Override
    public void setAngles(PopomEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) {
            this.head.pitch = 0;
            this.head.yaw = headYaw * (float) (Math.PI / 180.0) * 0.1F;
        } else {
            this.head.pitch = headPitch * (float) (Math.PI / 180.0) * 0.3F + entity.getHeadAngle(animationProgress);
            this.head.yaw = headYaw * (float) (Math.PI / 180.0) * 0.3F;
        }
        this.brleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.blleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.frleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.flleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mrleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mlleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.body0.roll = MathHelper.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body1.roll = MathHelper.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body2.roll = MathHelper.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body3.roll = MathHelper.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
    }
}
