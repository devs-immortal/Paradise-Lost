package net.id.paradise_lost.client.rendering.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.block.blockentity.PalaceDoorBlockEntity;
import net.id.paradise_lost.client.model.ParadiseLostModelLayers;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.inventory.InventoryMenu;

public class PalaceDoorBlockEntityRenderer implements BlockEntityRenderer<PalaceDoorBlockEntity> {

    public static final Material DOORS = new Material(InventoryMenu.BLOCK_ATLAS, ModConstants.id("block/palace_door"));

    private final ModelPart door;

    public PalaceDoorBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        ModelPart modelPart = context.bakeLayer(ParadiseLostModelLayers.PALACE_DOOR);
        this.door = modelPart.getChild("door");
    }

    public static LayerDefinition getTexturedModelData() {
        MeshDefinition modelData = new MeshDefinition();
        PartDefinition modelPartData = modelData.getRoot();

        PartDefinition door = modelPartData.addOrReplaceChild("door", CubeListBuilder.create()
                        .texOffs(0, 116).addBox(0.0F, -51.0F, -0.5F, 24.0F, 32.0F, 1.0F, CubeDeformation.NONE)
                        .texOffs(0, 0).addBox(0.0F, -112.0F, -2.0F, 24.0F, 112.0F, 4.0F, CubeDeformation.NONE),
                PartPose.offset(-24.0F, 24.0F, 0.0F));

        return LayerDefinition.create(modelData, 64, 160);
    }

    @Override
    public boolean shouldRenderOffScreen(PalaceDoorBlockEntity entity) {
        return true;
    }

    @Override
    public void render(PalaceDoorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumerProvider, int light, int overlay) {
        VertexConsumer vertexConsumer = DOORS.buffer(vertexConsumerProvider, RenderType::entityCutout);
        matrices.pushPose();
        matrices.rotateAround(Axis.XP.rotation(3.141592653589793F), 0.0F, 0.0F, 0.0F);
        matrices.translate(0.0, 0.0, 0.0);

        matrices.rotateAround(Axis.YP.rotation((float) Math.toRadians(entity.getRotation())), 0.5F, 0.0F, -0.5F);

        matrices.pushPose();
        matrices.rotateAround(Axis.YP.rotation(entity.getDoorAngle()), -1.0F, 0.0F, -0.5F);
        matrices.translate(0.5, 2.5, -0.5);
        door.render(matrices, vertexConsumer, light, overlay);
        matrices.popPose();

        matrices.pushPose();
        matrices.rotateAround(Axis.YP.rotation(-(entity.getDoorAngle()) + 3.141592653589793F), 2F, 0.0F, -0.5F);
        matrices.translate(3.5, 2.5, -0.5);
        door.render(matrices, vertexConsumer, light, overlay);
        matrices.popPose();

        matrices.popPose();
    }

}
