package net.id.paradise_lost.client.rendering.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.block.blockentity.IncubatorBlockEntity;
import net.id.paradise_lost.item.misc.MoaEggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.ItemDisplayContext;

public class IncubatorBlockEntityRenderer implements BlockEntityRenderer<IncubatorBlockEntity> {

    public IncubatorBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    private boolean isMoaEggInIncubator(IncubatorBlockEntity incubator) {
        return incubator.getItem().getItem() instanceof MoaEggItem;
    }

    @Override
    public void render(IncubatorBlockEntity entity, float tickDelta, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay) {
        if (entity.hasItem()) {
            if (isMoaEggInIncubator(entity)) {
                matrices.pushPose();
                matrices.translate(0.5, entity.getOffsetHeight(), 0.5);
                matrices.scale(1F, 1F, 5F);
                Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
                matrices.popPose();
            }
            else {
                matrices.pushPose();
                matrices.translate(0.5, entity.getOffsetHeight(), 0.5);
                matrices.scale(0.9F, 0.9F, 0.9F);
                Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItem(), ItemDisplayContext.FIXED, light, overlay, matrices, vertexConsumers, null, 0);
                matrices.popPose();
            }
        }
    }
}
