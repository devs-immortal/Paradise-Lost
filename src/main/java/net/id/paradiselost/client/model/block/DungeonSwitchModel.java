package net.id.paradiselost.client.model.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class DungeonSwitchModel extends Model {

    public ModelPart cube;

    public DungeonSwitchModel(BlockEntityRendererFactory.Context ctx) {
        super(RenderLayer::getEntityCutout);
        ModelPart modelPart = ctx.getLayerModelPart(ParadiseLostModelLayers.DUNGEON_SWITCH);
        cube = modelPart.getChild("cube");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        modelPartData.addChild("cube", ModelPartBuilder.create().uv(32, 0).cuboid(-4, -4, -4, 8, 8, 8), ModelTransform.pivot(8, 8, 8));
        return TexturedModelData.of(modelData, 64, 64);
    }

    public void render(MatrixStack matrixStack, VertexConsumer vertexConsumer, int i, int j) {
        render(matrixStack, vertexConsumer, i, j, 1, 1, 1, 1);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
        matrices.push();
        cube.render(matrices, vertices, light, overlay);
        matrices.pop();
    }
}
