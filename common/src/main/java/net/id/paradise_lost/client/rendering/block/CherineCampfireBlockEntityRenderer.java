package net.id.paradise_lost.client.rendering.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.id.paradise_lost.block.blockentity.CherineCampfireBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.CampfireBlock;

public class CherineCampfireBlockEntityRenderer implements BlockEntityRenderer<CherineCampfireBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CherineCampfireBlockEntityRenderer(BlockEntityRendererProvider.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }

    @Override
    public void render(CherineCampfireBlockEntity campfireBlockEntity, float f, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, int j) {
        Direction direction = campfireBlockEntity.getBlockState().getValue(CampfireBlock.FACING);
        NonNullList<ItemStack> defaultedList = campfireBlockEntity.getItemsBeingCooked();
        int k = (int) campfireBlockEntity.getBlockPos().asLong();

        for (int l = 0; l < defaultedList.size(); ++l) {
            ItemStack itemStack = defaultedList.get(l);
            if (itemStack != ItemStack.EMPTY) {
                matrixStack.pushPose();
                matrixStack.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction2 = Direction.from2DDataValue((l + direction.get2DDataValue()) % 4);
                float g = -direction2.toYRot();
                matrixStack.mulPose(Axis.YP.rotationDegrees(g));
                matrixStack.mulPose(Axis.XP.rotationDegrees(90.0F));
                matrixStack.translate(-0.3125D, -0.3125D, 0.0D);
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.FIXED, i, j, matrixStack, vertexConsumerProvider, campfireBlockEntity.getLevel(), k + l);
                matrixStack.popPose();
            }
        }
    }
}
