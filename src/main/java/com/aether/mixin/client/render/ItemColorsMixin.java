package com.aether.mixin.client.render;

import com.aether.blocks.AetherBlocks;
import net.minecraft.client.color.item.ItemColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemColors.class)
public class ItemColorsMixin {

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void create(CallbackInfoReturnable<ItemColors> info) {
        ItemColors origin = info.getReturnValue();
        origin.register(((stack, tintIndex) -> 0xbce632), AetherBlocks.SKYROOT_LEAVES.asItem(), AetherBlocks.SKYROOT_LEAF_PILE.asItem());
        origin.register(((stack, tintIndex) -> 0xB1FFCB), AetherBlocks.AETHER_GRASS_BLOCK.asItem(), AetherBlocks.AETHER_GRASS.asItem(), AetherBlocks.AETHER_TALL_GRASS.asItem(), AetherBlocks.AETHER_FERN.asItem(), AetherBlocks.AETHER_BUSH.asItem());
        info.setReturnValue(origin);
    }
}
