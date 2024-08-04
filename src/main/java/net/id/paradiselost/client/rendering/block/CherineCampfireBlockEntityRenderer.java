package net.id.paradiselost.client.rendering.block;

import net.id.paradiselost.blocks.blockentity.CherineCampfireBlockEntity;
import net.minecraft.block.CampfireBlock;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;

public class CherineCampfireBlockEntityRenderer implements BlockEntityRenderer<CherineCampfireBlockEntity> {

    private final ItemRenderer itemRenderer;

    public CherineCampfireBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        this.itemRenderer = ctx.getItemRenderer();
    }
    
    @Override
    public void render(CherineCampfireBlockEntity campfireBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        Direction direction = campfireBlockEntity.getCachedState().get(CampfireBlock.FACING);
        DefaultedList<ItemStack> defaultedList = campfireBlockEntity.getItemsBeingCooked();
        int k = (int) campfireBlockEntity.getPos().asLong();
        
        for (int l = 0; l < defaultedList.size(); ++l) {
            ItemStack itemStack = defaultedList.get(l);
            if (itemStack != ItemStack.EMPTY) {
                matrixStack.push();
                matrixStack.translate(0.5D, 0.44921875D, 0.5D);
                Direction direction2 = Direction.fromHorizontal((l + direction.getHorizontal()) % 4);
                float g = -direction2.asRotation();
                matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(g));
                matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                matrixStack.translate(-0.3125D, -0.3125D, 0.0D);
                matrixStack.scale(0.375F, 0.375F, 0.375F);
                this.itemRenderer.renderItem(itemStack, ModelTransformationMode.FIXED, i, j, matrixStack, vertexConsumerProvider, campfireBlockEntity.getWorld(), k + l);
                matrixStack.pop();
            }
        }
    }
}
