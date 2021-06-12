package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(method = "getGrowthSpeed", at = @At("RETURN"), cancellable = true)
    private static void getCropGrowthSpeed(Block block, BlockGetter view, BlockPos pos, CallbackInfoReturnable<Float> ci) {
        float newSpeed = ci.getReturnValue();
        BlockPos blockPos_2 = pos.below();

        for (int rangeX = -1; rangeX <= 1; ++rangeX) {
            for (int rangeZ = -1; rangeZ <= 1; ++rangeZ) {
                float extraSpeed = 0.0F;
                BlockState blockState_1 = view.getBlockState(blockPos_2.offset(rangeX, 0, rangeZ));

                if (blockState_1.getBlock() == AetherBlocks.AETHER_FARMLAND) {
                    extraSpeed = 1.0F;

                    if (blockState_1.getValue(FarmBlock.MOISTURE) > 0) extraSpeed = 3.0F;
                }
                if (rangeX != 0 || rangeZ != 0) extraSpeed /= 4.0F;
                newSpeed += extraSpeed;
            }
        }
        ci.setReturnValue(newSpeed);
    }

    @Inject(method = "mayPlaceOn", at = @At("TAIL"), cancellable = true)
    protected void canPlantOnTop(BlockState floor, BlockGetter world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(
                cir.getReturnValue() || floor.is(AetherBlocks.AETHER_FARMLAND)
        );
    }
}