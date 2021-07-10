package com.aether.mixin.item;

import com.aether.blocks.AetherBlocks;
import com.aether.util.RegistryUtil;
import com.aether.world.dimension.AetherDimension;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidFillable;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.BucketItem;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BucketItem.class)
public class BucketItemMixin {

    private final Fluid fluid;

    public BucketItemMixin(Fluid fluid) {
        this.fluid = fluid;
    }

    @Inject(at = @At("HEAD"), method = "placeFluid", cancellable = true)
    public void placeFluid(@Nullable PlayerEntity player, World world, BlockPos pos, @Nullable BlockHitResult hitResult, CallbackInfoReturnable<Boolean> cir) {
        if (this.fluid instanceof FlowableFluid) {
             if (RegistryUtil.dimensionMatches(world, AetherDimension.TYPE) && this.fluid.isIn(FluidTags.LAVA)) {
                int i = pos.getX();
                int j = pos.getY();
                int k = pos.getZ();
                world.playSound(player, pos, SoundEvents.BLOCK_GLASS_PLACE, SoundCategory.BLOCKS, 1.0F, 1.0F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
                world.setBlockState(pos, AetherBlocks.AEROGEL.getDefaultState());

                 for(int l = 0; l < 8; ++l) {
                    world.addParticle(ParticleTypes.CLOUD, (double)i + Math.random(), (double)j + Math.random(), (double)k + Math.random(), 0.0D, 0.0D, 0.0D);
                }
                cir.setReturnValue(true);
            }
        }
    }
}
