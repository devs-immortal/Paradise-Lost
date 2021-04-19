package com.aether.mixin.structure;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.Block;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class FeatureMixin {

    @Inject(method = {"isSoil(Lnet/minecraft/block/Block;)Z"}, at = {@At("HEAD")}, cancellable = true)
    private static void isSoil(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block == AetherBlocks.AETHER_DIRT || block == AetherBlocks.AETHER_GRASS_BLOCK || block == AetherBlocks.AETHER_ENCHANTED_GRASS || block == AetherBlocks.AETHER_FARMLAND) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }

    @Inject(method = {"isStone"}, at = {@At("HEAD")}, cancellable = true)
    private static void isStone(Block block, CallbackInfoReturnable<Boolean> cir) {
        if (block == AetherBlocks.HOLYSTONE || block == AetherBlocks.MOSSY_HOLYSTONE) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
