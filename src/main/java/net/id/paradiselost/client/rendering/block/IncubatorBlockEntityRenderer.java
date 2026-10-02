package net.id.paradiselost.client.rendering.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.paradiselost.blocks.blockentity.IncubatorBlockEntity;
import net.id.paradiselost.items.misc.MoaEggItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

@Environment(EnvType.CLIENT)
public class IncubatorBlockEntityRenderer implements BlockEntityRenderer<IncubatorBlockEntity> {

    public IncubatorBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    private boolean isMoaEggInIncubator(IncubatorBlockEntity incubator) {
        return incubator.getItem().getItem() instanceof MoaEggItem;
    }

    @Override
    public void render(IncubatorBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.hasItem()) {
            if (isMoaEggInIncubator(entity)) {
                matrices.push();
                matrices.translate(0.5, entity.getOffsetHeight(), 0.5);
                matrices.scale(1F, 1F, 5F); //Thick egg
                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getItem(), ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
                matrices.pop();
            }
            else {
                matrices.push();
                matrices.translate(0.5, entity.getOffsetHeight(), 0.5);
                matrices.scale(0.9F, 0.9F, 0.9F);
                MinecraftClient.getInstance().getItemRenderer().renderItem(entity.getItem(), ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
                matrices.pop();
            }
        }
    }
}
