package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "canPlantOnTop", at = @At("TAIL"), cancellable = true)
    protected void canPlantOnTop(BlockState floor, BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                cir.getReturnValue() || floor.isOf(AetherBlocks.AETHER_FARMLAND)
        );
    }

    @Inject(method = "getAvailableMoisture", at = @At("RETURN"), cancellable = true)
    private static void getCropGrowthSpeed(Block block, BlockView view, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        float newSpeed = ci.getReturnValue();
        BlockPos blockPos_2 = pos.down();

        for (int rangeX = -1; rangeX <= 1; ++rangeX) {
            for (int rangeZ = -1; rangeZ <= 1; ++rangeZ) {
                float extraSpeed = 0.0F;
                BlockState blockState_1 = view.getBlockState(blockPos_2.add(rangeX, 0, rangeZ));

                if (blockState_1.getBlock() == AetherBlocks.AETHER_FARMLAND) {
                    extraSpeed = 1.0F;

                    if (blockState_1.get(FarmlandBlock.MOISTURE) > 0) {
                        extraSpeed = 3.0F;
                    }
                }

                if (rangeX != 0 || rangeZ != 0) {
                    extraSpeed /= 4.0F;
                }

                newSpeed += extraSpeed;
            }
        }

        ci.setReturnValue(newSpeed);
    }
}
