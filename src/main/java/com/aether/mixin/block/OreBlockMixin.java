package com.aether.mixin.block;

import com.aether.blocks.AetherBlocks;
import net.minecraft.block.OreBlock;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@SuppressWarnings("ConstantConditions")
@Mixin(OreBlock.class)
public abstract class OreBlockMixin {
    //TODO: Tweak the xp values
    @Inject(method = "getExperienceWhenMined", at = @At("HEAD"), cancellable = true)
    protected void getExperience(Random random, CallbackInfoReturnable<Integer> cir) {
        if ((Object) this == AetherBlocks.ZANITE_ORE) {
            cir.setReturnValue(MathHelper.nextInt(random, 0, 2));
        } else if ((Object) this == AetherBlocks.AMBROSIUM_ORE) {
            cir.setReturnValue(MathHelper.nextInt(random, 0, 2));
        } else if ((Object) this == AetherBlocks.GRAVITITE_ORE) {
            cir.setReturnValue(MathHelper.nextInt(random, 0, 2));
        }
    }
}
