package net.id.paradiselost.client.model.entity;

import net.id.paradiselost.client.rendering.entity.state.PopomEntityRenderState;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.util.math.MathHelper;

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

    public static TexturedModelData getTexturedModelData() {
        ModelData ModelData = new ModelData();
        ModelPartData root = ModelData.getRoot();

        root.addChild("body0", ModelPartBuilder.create().uv(0, 78).cuboid(-5.0F, -9.0F, -8.0F, 10.0F, 6.0F, 15.0F, new Dilation(1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        root.addChild("body1", ModelPartBuilder.create().uv(0, 54).cuboid(-5.5F, -11.0F, -8.0F, 11.0F, 8.0F, 16.0F, new Dilation(1.05F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        root.addChild("body2", ModelPartBuilder.create().uv(0, 28).cuboid(-6.0F, -12.0F, -8.0F, 12.0F, 9.0F, 17.0F, new Dilation(1.1F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        root.addChild("body3", ModelPartBuilder.create().uv(0, 0).cuboid(-7.0F, -13.0F, -8.0F, 14.0F, 10.0F, 18.0F, new Dilation(1.15F)), ModelTransform.pivot(0.0F, 24.0F, 0.0F));

        root.addChild("head", ModelPartBuilder.create().uv(38, 54).cuboid(-4.0F, -2.0F, -4.0F, 8.0F, 4.0F, 4.0F)
                .uv(38, 62).cuboid(-3.0F, -5.0F, -5.0F, 6.0F, 3.0F, 3.0F), ModelTransform.pivot(0.0F, 19.0F, -8.0F));

        root.addChild("frleg", ModelPartBuilder.create().uv(0, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, -6.0F));

        root.addChild("flleg", ModelPartBuilder.create().uv(0, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, -6.0F));

        root.addChild("brleg", ModelPartBuilder.create().uv(12, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, 4.0F));

        root.addChild("blleg", ModelPartBuilder.create().uv(12, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, 4.0F));

        root.addChild("mrleg", ModelPartBuilder.create().uv(12, 116).cuboid(-2.99F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(-2.0F, 21.0F, -1.0F));

        root.addChild("mlleg", ModelPartBuilder.create().uv(12, 122).cuboid(-0.01F, 0.0F, -1.0F, 3.0F, 3.0F, 3.0F), ModelTransform.pivot(2.0F, 21.0F, -1.0F));

        return TexturedModelData.of(ModelData, 64, 128);
    }

    protected ModelPart[] getFurs() {
        return new ModelPart[]{this.body0, this.body1, this.body2, this.body3};
    }

    private ModelPart[] getLegs() {
        return new ModelPart[]{this.frleg, this.flleg, this.brleg, this.blleg, this.mrleg, this.mlleg};
    }

    @Override
    public void setAngles(PopomEntityRenderState state) {
        super.setAngles(state);
        if (state.baby) {
            this.head.pitch = 0;
            this.head.yaw = state.yawDegrees * (float) (Math.PI / 180.0) * 0.1F;
        } else {
            this.head.pitch = state.pitch * (float) (Math.PI / 180.0) * 0.3F + state.headAngle;
            this.head.yaw = state.yawDegrees * (float) (Math.PI / 180.0) * 0.3F;
        }
        float limbAngle = state.limbFrequency;
        float limbDistance = state.limbAmplitudeMultiplier;
        this.brleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.blleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.frleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        this.flleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mrleg.pitch = MathHelper.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
        this.mlleg.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
        float bodyRoll = MathHelper.cos(limbAngle * 0.9F) * 0.3F * limbDistance;
        this.body0.roll = bodyRoll;
        this.body1.roll = bodyRoll;
        this.body2.roll = bodyRoll;
        this.body3.roll = bodyRoll;

        ModelPart visibleFur = state.baby ? this.body1 : this.getFurs()[Math.min(3, state.furSize)];
        for (ModelPart fur : this.getFurs()) {
            fur.visible = fur == visibleFur;
        }

        if (state.baby) {
            // replicates the old AnimalModel(true, 25, 2, 3, 3, 48) child transform plus
            // the custom half-scale fur from the 1.21.1 render override
            applyChildTransform(this.head, 0.5F, 25.0F, 2.0F);
            for (ModelPart leg : this.getLegs()) {
                applyChildTransform(leg, 1.0F / 3.0F, 48.0F, 0.0F);
            }
            applyChildTransform(this.body1, 0.5F, 25.6F, 0.0F);
        }
    }

    private static void applyChildTransform(ModelPart part, float scale, float yOffset, float zOffset) {
        part.pivotX *= scale;
        part.pivotY = (part.pivotY + yOffset) * scale;
        part.pivotZ = (part.pivotZ + zOffset) * scale;
        part.xScale *= scale;
        part.yScale *= scale;
        part.zScale *= scale;
    }
}
