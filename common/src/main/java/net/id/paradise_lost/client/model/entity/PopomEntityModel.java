package net.id.paradise_lost.client.model.entity;

import net.id.paradise_lost.client.rendering.entity.state.PopomEntityRenderState;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;

public class PopomEntityModel extends EntityModel<PopomEntityRenderState> {

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
        super(root);
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

    private ModelPart[] getLegs() {
        return new ModelPart[]{this.frleg, this.flleg, this.brleg, this.blleg, this.mrleg, this.mlleg};
    }

    protected ModelPart[] getFurs() {
        return new ModelPart[]{this.body0, this.body1, this.body2, this.body3};
    }

    @Override
    public void setupAnim(PopomEntityRenderState state) {
        super.setupAnim(state);
        if (state.isBaby) {
            this.head.xRot = 0;
            this.head.yRot = state.yRot * (float) (Math.PI / 180.0) * 0.1F;
        } else {
            this.head.xRot = state.xRot * (float) (Math.PI / 180.0) * 0.3F + state.headAngle;
            this.head.yRot = state.yRot * (float) (Math.PI / 180.0) * 0.3F;
        }
        float limbAngle = state.walkAnimationPos;
        float limbDistance = state.walkAnimationSpeed;
        this.brleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.blleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.frleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.flleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mrleg.xRot = Mth.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mlleg.xRot = Mth.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        float bodyRoll = Mth.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body0.zRot = bodyRoll;
        this.body1.zRot = bodyRoll;
        this.body2.zRot = bodyRoll;
        this.body3.zRot = bodyRoll;

        ModelPart visibleFur = state.isBaby ? this.body1 : this.getFurs()[Math.min(3, state.furSize)];
        for (ModelPart fur : this.getFurs()) {
            fur.visible = fur == visibleFur;
        }

        if (state.isBaby) {
            // replicates the old AgeableListModel(true, 25, 2, 3, 3, 48) child transform plus
            // the custom half-scale fur from the 1.21.1 render override
            applyChildTransform(this.head, 0.5F, 25.0F, 2.0F);
            for (ModelPart leg : this.getLegs()) {
                applyChildTransform(leg, 1.0F / 3.0F, 48.0F, 0.0F);
            }
            applyChildTransform(this.body1, 0.5F, 25.6F, 0.0F);
        }
    }

    private static void applyChildTransform(ModelPart part, float scale, float yOffset, float zOffset) {
        part.x *= scale;
        part.y = (part.y + yOffset) * scale;
        part.z = (part.z + zOffset) * scale;
        part.xScale *= scale;
        part.yScale *= scale;
        part.zScale *= scale;
    }
}
