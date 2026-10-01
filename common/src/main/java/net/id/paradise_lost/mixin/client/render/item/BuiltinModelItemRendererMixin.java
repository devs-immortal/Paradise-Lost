package net.id.paradise_lost.mixin.client.render.item;

import com.mojang.blaze3d.vertex.PoseStack;
import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.block.blockentity.CalciteDecoratedPotBlockEntity;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BuiltinModelItemRendererMixin {

    private final CalciteDecoratedPotBlockEntity renderCalciteDecoratedPot = new CalciteDecoratedPotBlockEntity(BlockPos.ZERO, BlockRegistry.CALCITE_DECORATED_POT.get().defaultBlockState());

    @Final
    @Shadow
    private BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack stack, ItemDisplayContext mode, PoseStack matrices, MultiBufferSource vertexConsumers, int light, int overlay, CallbackInfo ci) {
        Item item = stack.getItem();
        if (item instanceof BlockItem) {
            BlockState blockState = ((BlockItem) item).getBlock().defaultBlockState();
            if (blockState.is(BlockRegistry.CALCITE_DECORATED_POT.get())) {
                this.renderCalciteDecoratedPot.readFrom(stack);
                this.blockEntityRenderDispatcher.renderItem(this.renderCalciteDecoratedPot, matrices, vertexConsumers, light, overlay);
                ci.cancel();
            }
        }
    }
}
