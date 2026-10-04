package net.id.paradise_lost.client.rendering.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.id.paradise_lost.block.blockentity.*;
import net.id.paradise_lost.block.mechanical.TreeTapBlock;
import net.minecraft.client.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;

public class TreeTapBlockEntityRenderer implements BlockEntityRenderer<TreeTapBlockEntity> {

    public TreeTapBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(TreeTapBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (!entity.isEmpty()) {
            Direction facing = entity.getBlockState().getValue(TreeTapBlock.FACING);
            matrices.pushPose();
            matrices.translate(0.5, 0.4, 0.5);
            matrices.mulPose(Axis.YP.rotationDegrees(-facing.toYRot() + 180F));
            matrices.scale(0.75F, 0.75F, 0.75F);
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(0), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
            matrices.popPose();
        }
    }

}
