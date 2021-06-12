package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FarmBlock.class)
public class FarmlandBlockMixin {
    @Inject(method = "setToDirt", at = @At("HEAD"), cancellable = true)
    private static void onSetToDirt(BlockState state, Level world, BlockPos pos, CallbackInfo ci) {
        if (state.is(AetherBlocks.AETHER_FARMLAND)) {
            world.setBlockAndUpdate(pos, Block.pushEntitiesUp(state, AetherBlocks.AETHER_DIRT.defaultBlockState(), world, pos));
            ci.cancel();
        }
    }
}
