package net.id.aether.client.rendering.block;

import net.id.aether.blocks.blockentity.IncubatorBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;

public class IncubatorBlockEntityRenderer implements BlockEntityRenderer<IncubatorBlockEntity> {

    public IncubatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(IncubatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (!entity.isEmpty()) {
            matrices.push();
            matrices.translate(0.5, 0.55, 0.5);
            matrices.scale(0.9F, 0.9F, 0.9F);
            MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getStack(0), ModelTransformation.Mode.FIXED, light, overlay, matrices, vertexConsumers, 0);
            matrices.pop();
        }
    }
}
