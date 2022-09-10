package net.id.paradiselost.mixin.block;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {
    @Inject(method = "setToDirt", at = @At("HEAD"), cancellable = true)
    private static void onSetToDirt(BlockState state, World world, BlockPos pos, CallbackInfo ci) {
        if (state.isOf(ParadiseLostBlocks.FARMLAND)) {
            world.setBlockState(pos, Block.pushEntitiesUpBeforeBlockChange(state, ParadiseLostBlocks.DIRT.getDefaultState(), world, pos));
            ci.cancel();
        }
    }
}
