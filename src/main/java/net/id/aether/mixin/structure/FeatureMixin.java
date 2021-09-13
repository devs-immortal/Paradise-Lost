package net.id.aether.mixin.structure;

import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.Feature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Feature.class)
public class FeatureMixin {

    // replace with AetherFeature class and static isAetherStone method
    @Inject(method = "isStone", at = @At("HEAD"), cancellable = true)
    private static void isStone(BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (state.getBlock() == AetherBlocks.HOLYSTONE || state.getBlock() == AetherBlocks.MOSSY_HOLYSTONE) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
