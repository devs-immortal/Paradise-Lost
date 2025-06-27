package net.id.paradiselost.mixin.client.render.item;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.DecoratedPotBlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltinModelItemRenderer.class)
public class BuiltinModelItemRendererMixin {

    private final CalciteDecoratedPotBlockEntity renderCalciteDecoratedPot = new CalciteDecoratedPotBlockEntity(BlockPos.ORIGIN, ParadiseLostBlocks.CALCITE_DECORATED_POT.getDefaultState());

    @Final
    @Shadow
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ModelTransformationMode mode, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockState blockState = ((BlockItem)item).getBlock().getDefaultState();
            if (blockState.isOf(ParadiseLostBlocks.CALCITE_DECORATED_POT)) {
                this.renderCalciteDecoratedPot.readFrom(stack);
                this.blockEntityRenderDispatcher.renderEntity(this.renderCalciteDecoratedPot, matrices, vertexConsumers, light, overlay);
                ci.cancel();
            }
        }
    }
}
