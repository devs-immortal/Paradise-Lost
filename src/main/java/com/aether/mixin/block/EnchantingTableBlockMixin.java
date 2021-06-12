package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(EnchantmentTableBlock.class)
public class EnchantingTableBlockMixin {

    @Inject(at = @At("HEAD"), method = "randomDisplayTick")
    public void randomDisplayTick(BlockState state, Level world, BlockPos pos, Random random, CallbackInfo ci) {
        for (int i = -2; i <= 2; ++i) {
            for (int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) j = 2;
                if (random.nextInt(16) == 0) {
                    for (int k = 0; k <= 1; ++k) {
                        BlockPos blockPos = pos.offset(i, k, j);
                        if (world.getBlockState(blockPos).is(AetherBlocks.SKYROOT_BOOKSHELF)) {
                            if (!world.isEmptyBlock(pos.offset(i / 2, 0, j / 2))) break;
                            world.addParticle(ParticleTypes.ENCHANT, (double) pos.getX() + 0.5D, (double) pos.getY() + 2.0D, (double) pos.getZ() + 0.5D, (double) ((float) i + random.nextFloat()) - 0.5D, (float) k - random.nextFloat() - 1.0F, (double) ((float) j + random.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }
}
