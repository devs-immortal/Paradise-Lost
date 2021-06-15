package com.aether.mixin.client.render;

import com.aether.blocks.AetherBlocks;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockColors.class)
public class BlockColorsMixin {

    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void create(CallbackInfoReturnable<BlockColors> info) {
        BlockColors origin = info.getReturnValue();
        origin.registerColorProvider(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), AetherBlocks.SKYROOT_LEAVES, AetherBlocks.SKYROOT_LEAF_PILE);
        origin.registerColorProvider(((state, world, pos, tintIndex) ->  world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_GRASS, AetherBlocks.AETHER_TALL_GRASS, AetherBlocks.AETHER_FERN, AetherBlocks.AETHER_BUSH);
        info.setReturnValue(origin);
    }
}
