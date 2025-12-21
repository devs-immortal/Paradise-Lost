package net.id.paradiselost.client.rendering.block;

import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.blocks.blockentity.PalaceDoorBlockEntity;
import net.id.paradiselost.client.model.ParadiseLostModelLayers;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.math.RotationAxis;

public class PalaceDoorBlockEntityRenderer implements BlockEntityRenderer<PalaceDoorBlockEntity> {

    public static final SpriteIdentifier DOORS = new SpriteIdentifier(PlayerScreenHandler.BLOCK_ATLAS_TEXTURE, ParadiseLost.locate("block/palace_door"));

    private final ModelPart door;

    public PalaceDoorBlockEntityRenderer(BlockEntityRendererFactory.Context context) {
        ModelPart modelPart = context.getLayerModelPart(ParadiseLostModelLayers.PALACE_DOOR);
        this.door = modelPart.getChild("door");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
        
        ModelPartData door = modelPartData.addChild("door", ModelPartBuilder.create()
                        .uv(0, 116).cuboid(0.0F, -51.0F, -0.5F, 24.0F, 32.0F, 1.0F, Dilation.NONE)
                        .uv(0, 0).cuboid(0.0F, -112.0F, -2.0F, 24.0F, 112.0F, 4.0F, Dilation.NONE),
                ModelTransform.pivot(-24.0F, 24.0F, 0.0F));

        return TexturedModelData.of(modelData, 64, 160);
    }

    @Override
    public void render(PalaceDoorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {
        VertexConsumer vertexConsumer = DOORS.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntityCutout);
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_X.rotation(3.141592653589793F), 0.0F, 0.0F, 0.0F);
        matrices.translate(0.0, 0.0, 0.0);

        matrices.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.toRadians(entity.getRotation())), 0.5F, 0.0F, -0.5F);
        // left door
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(entity.getDoorAngle()), -1.0F, 0.0F, -0.5F);
        matrices.translate(0.5, 2.5, -0.5);
        door.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();
        // right door
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(-(entity.getDoorAngle()) + 3.141592653589793F), 2F, 0.0F, -0.5F);
        matrices.translate(3.5, 2.5, -0.5);
        door.render(matrices, vertexConsumer, light, overlay);
        matrices.pop();

        matrices.pop();
    }

}
