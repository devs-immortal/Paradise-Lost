package net.id.paradise_lost.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.id.paradise_lost.entity.passive.PopomEntity;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class PopomEntityModel<T extends PopomEntity> extends AgeableListModel<T> {

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

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition ModelData = new MeshDefinition();
        PartDefinition root = ModelData.getRoot();

        PartDefinition body0 = root.addOrReplaceChild("body0", CubeListBuilder.create().texOffs(0, 78).addBox(-5.0F, -9.0F, -8.0F, 10.0F, 6.0F, 15.0F, new CubeDeformation(1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body1 = root.addOrReplaceChild("body1", CubeListBuilder.create().texOffs(0, 54).addBox(-5.5F, -11.0F, -8.0F, 11.0F, 8.0F, 16.0F, new CubeDeformation(1.05F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body2 = root.addOrReplaceChild("body2", CubeListBuilder.create().texOffs(0, 28).addBox(-6.0F, -12.0F, -8.0F, 12.0F, 9.0F, 17.0F, new CubeDeformation(1.1F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition body3 = root.addOrReplaceChild("body3", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -13.0F, -8.0F, 14.0F, 10.0F, 18.0F, new CubeDeformation(1.15F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(38, 54).addBox(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 4.0F)
                .texOffs(38, 62).addBox(-3.0F, -5.0F, -5.0F, 6.0F, 3.0F, 3.0F), PartPose.offset(0.0F, 19.0F, -8.0F));

        PartDefinition frleg = root.addOrReplaceChild("frleg", CubeListBuilder.create().texOffs(0, 116).addBox(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(-2.0F, 21.0F, -6.0F));

        PartDefinition flleg = root.addOrReplaceChild("flleg", CubeListBuilder.create().texOffs(0, 122).addBox(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(2.0F, 21.0F, -6.0F));

        PartDefinition brleg = root.addOrReplaceChild("brleg", CubeListBuilder.create().texOffs(12, 116).addBox(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(-2.0F, 21.0F, 4.0F));

        PartDefinition blleg = root.addOrReplaceChild("blleg", CubeListBuilder.create().texOffs(12, 122).addBox(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(2.0F, 21.0F, 4.0F));

        PartDefinition mrleg = root.addOrReplaceChild("mrleg", CubeListBuilder.create().texOffs(12, 116).addBox(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(-2.0F, 21.0F, -1.0F));

        PartDefinition mlleg = root.addOrReplaceChild("mlleg", CubeListBuilder.create().texOffs(12, 122).addBox(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), PartPose.offset(2.0F, 21.0F, -1.0F));

        return LayerDefinition.create(ModelData, 64, 128);
    }

    @Override
    public void renderToBuffer(PoseStack matrices, VertexConsumer vertices, int light, int overlay, int color) {
        super.renderToBuffer(matrices, vertices, light, overlay, color);
        if (this.young) {
            matrices.pushPose();
            float f = 0.5F;
            matrices.scale(f, f, f);
            matrices.translate(0.0F, 1.6F, 0.0F);
            this.getFurs()[1].render(matrices, vertices, light, overlay, color);
            matrices.popPose();
        } else {
            this.getFurs()[Math.min(3, furSize)].render(matrices, vertices, light, overlay, color);
        }
    }

    @Override
    protected Iterable<ModelPart> headParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> bodyParts() {
        return ImmutableList.of(this.frleg, this.flleg, this.brleg, this.blleg, this.mrleg, this.mlleg);
    }

    protected ModelPart[] getFurs() {
        return new ModelPart[]{this.body0, this.body1, this.body2, this.body3};
    }

    @Override
    public void setupAnim(PopomEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch) {
        if (entity.isBaby()) {
            this.head.xRot = 0;
            this.head.yRot = headYaw * (float) (Math.PI / 180.0) * 0.1F;
        } else {
            this.head.xRot = headPitch * (float) (Math.PI / 180.0) * 0.3F + entity.getHeadAngle(animationProgress);
            this.head.yRot = headYaw * (float) (Math.PI / 180.0) * 0.3F;
        }
        this.brleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.blleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.frleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.flleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mrleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mlleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.body0.zRot = Mth.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body1.zRot = Mth.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body2.zRot = Mth.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body3.zRot = Mth.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
    }
}
