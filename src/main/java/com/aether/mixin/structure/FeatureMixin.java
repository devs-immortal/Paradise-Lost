package com.aether.mixin.structure;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class FeatureMixin {

    @Inject(method = {"isSoil(Lnet/minecraft/block/BlockState;)Z"}, at = {@At("HEAD")}, cancellable = true)
    private static void isSoil(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == AetherBlocks.AETHER_DIRT || state.getBlock() == AetherBlocks.AETHER_GRASS_BLOCK || state.getBlock() == AetherBlocks.AETHER_ENCHANTED_GRASS || state.getBlock() == AetherBlocks.AETHER_FARMLAND) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = {"isStone"}, at = {@At("HEAD")}, cancellable = true)
    private static void isStone(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == AetherBlocks.HOLYSTONE || state.getBlock() == AetherBlocks.MOSSY_HOLYSTONE) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
