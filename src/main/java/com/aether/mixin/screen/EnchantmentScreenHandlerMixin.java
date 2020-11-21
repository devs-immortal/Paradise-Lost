package com.aether.mixin.screen;

import net.minecraft.screen.EnchantmentScreenHandler;
import org.spongepowered.asm.mixin.Mixin;

//@SuppressWarnings("UnresolvedMixinReference")
@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin {

//    @Redirect(method = "method_17411",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
//    public boolean isOfReplaced(BlockState blockState, Block block) {
//        return blockState.getBlock().is(block) || blockState.getBlock().is(AetherBlocks.SKYROOT_BOOKSHELF);
//    }
}

