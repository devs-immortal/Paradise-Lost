package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.state.property.IntProperty;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LeavesBlock.class)
public class LeavesBlockMixin {

    @Final
    @Shadow
    public static final IntProperty DISTANCE = IntProperty.of("distance", 1, 13);

    @Inject(method = {"updateDistanceFromLogs"}, at = @At("HEAD"), cancellable = true)
    private static void updateDistanceFromLogs(BlockState state, WorldAccess world, BlockPos pos, CallbackInfoReturnable<BlockState> cir) {
        if((state.isOf(AetherBlocks.GOLDEN_OAK_LEAVES) || state.isOf(AetherBlocks.FROST_WISTERIA_LEAVES) || state.isOf(AetherBlocks.ROSE_WISTERIA_LEAVES) || state.isOf(AetherBlocks.LAVENDER_WISTERIA_LEAVES))) {
            int i = 13;
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            Direction[] var5 = Direction.values();
            int var6 = var5.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                Direction direction = var5[var7];
                mutable.set(pos, direction);
                i = Math.min(i, getDistance(world.getBlockState(mutable)) + 1);
                if (i == 1) {
                    break;
                }
            }
            cir.setReturnValue(state.with(DISTANCE, i));
            cir.cancel();
        }
    }

    private static int getDistance(BlockState state) {
        if (BlockTags.LOGS.contains(state.getBlock())) {
            return 0;
        } else {
            return state.getBlock() instanceof LeavesBlock ? state.get(DISTANCE) : 13;
        }
    }
}
