package com.aether.mixin.screen;

import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;

//@SuppressWarnings("UnresolvedMixinReference")
@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentScreenHandlerMixin {

//    @Redirect(method = "method_17411",
//            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;isOf(Lnet/minecraft/block/Block;)Z"))
//    public boolean isOfReplaced(BlockState blockState, Block block) {
//        return blockState.getBlock().is(block) || blockState.getBlock().is(AetherBlocks.SKYROOT_BOOKSHELF);
//    }
}