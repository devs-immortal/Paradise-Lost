package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlantBlock.class)
public class PlantBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("TAIL"), cancellable = true)
    protected void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                cir.getReturnValue() || (
                        floor.isOf(AetherBlocks.aether_grass) || floor.isOf(AetherBlocks.aether_dirt)
                                || floor.isOf(AetherBlocks.aether_farmland) || floor.isOf(AetherBlocks.aether_enchanted_grass)
                )
        );
    }
}
