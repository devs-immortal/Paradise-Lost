package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.*;
import net.id.paradiselost.blocks.blockentity.*;
import net.minecraft.client.*;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.*;
import net.minecraft.client.render.model.json.*;
import net.minecraft.client.util.math.*;

@Environment(EnvType.CLIENT)
public class TreeTapBlockEntityRenderer implements BlockEntityRenderer<TreeTapBlockEntity> {

    public TreeTapBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(TreeTapBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.isEmpty()) {
            matrices.push();
			// todo: rotate along facing axis
            matrices.translate(0.5, 0.4, 0.5);
            matrices.scale(0.75F, 0.75F, 0.75F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }
    }

}
