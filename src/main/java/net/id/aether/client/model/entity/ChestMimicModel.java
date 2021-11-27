package net.id.aether.client.model.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.entities.hostile.ChestMimicEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class ChestMimicModel extends EntityModel<ChestMimicEntity> {

    public final ModelPart box, boxLid, leftLeg, rightLeg;

    public ChestMimicModel(ModelPart root) {
        this.box = root.getChild("box");
        this.boxLid = root.getChild("boxLid");
        this.leftLeg = root.getChild("leftLeg");
        this.rightLeg = root.getChild("rightLeg");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("box", ModelPartBuilder.create().uv(0, 0).cuboid(-8F, 0F, -8F, 16, 10, 16), ModelTransform.pivot(0F, -24F, 0F));
        modelPartData.addChild("boxLid", ModelPartBuilder.create().uv(16, 10).cuboid(0F, 0F, 0F, 16, 6, 16), ModelTransform.pivot(-8F, -24F, 8F));
        modelPartData.addChild("leftLeg", ModelPartBuilder.create().uv(0, 0).cuboid(-3F, 0F, -3F, 6, 15, 6), ModelTransform.pivot(-4F, -15F, 0F));
        modelPartData.addChild("rightLeg", ModelPartBuilder.create().uv(0, 0).cuboid(-3F, 0F, -3F, 6, 15, 6), ModelTransform.pivot(4F, -15F, 0F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(ChestMimicEntity entityIn, float f, float f1, float f2, float f3, float f4) {
        this.boxLid.pivotX = 3.14159265F - entityIn.mouth;
        this.rightLeg.pivotX = entityIn.legs;
        this.leftLeg.pivotX = -entityIn.legs;
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
        box.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        boxLid.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        leftLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
        rightLeg.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
    }
}
