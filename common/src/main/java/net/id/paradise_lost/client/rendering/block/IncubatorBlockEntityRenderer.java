package net.id.paradise_lost.client.rendering.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.id.paradise_lost.block.blockentity.IncubatorBlockEntity;
import net.id.paradise_lost.item.misc.MoaEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

public class IncubatorBlockEntityRenderer implements BlockEntityRenderer<IncubatorBlockEntity> {

    public IncubatorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    private boolean isMoaEggInIncubator(IncubatorBlockEntity incubator) {
        return incubator.getItem().getItem() instanceof MoaEggItem;
    }

    @Override
    public void render(IncubatorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.hasItem()) {
            return;
        }

        matrices.pushPose();
        matrices.translate(0.5, entity.getOffsetHeight(), 0.5);
        matrices.mulPose(Axis.YP.rotationDegrees(yRotForFacing(entity.getItemFacing())));
        if (isMoaEggInIncubator(entity)) {
            matrices.scale(1F, 1F, 5F);
        } else {
            matrices.scale(0.9F, 0.9F, 0.9F);
        }
        Minecraft.getInstance().getItemRenderer().renderStatic(
                entity.getItem(),
                ItemDisplayContext.FIXED,
                light,
                overlay,
                matrices,
                vertexConsumers,
                null,
                0
        );
        matrices.popPose();
    }

    private static float yRotForFacing(Direction facing) {
        return switch (facing) {
            case EAST -> 90.0F;
            case NORTH -> 180.0F;
            case WEST -> 270.0F;
            default -> 0.0F;
        };
    }
}
