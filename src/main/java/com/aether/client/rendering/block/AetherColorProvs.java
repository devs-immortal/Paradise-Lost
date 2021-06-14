package com.aether.client.rendering.block;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;

public class AetherColorProvs {

    public static void initClient() {
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), AetherBlocks.SKYROOT_LEAVES, AetherBlocks.SKYROOT_LEAF_PILE);
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) ->  world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_GRASS, AetherBlocks.AETHER_TALL_GRASS, AetherBlocks.AETHER_FERN, AetherBlocks.AETHER_BUSH);
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> 0xbce632), AetherBlocks.SKYROOT_LEAVES.asItem(), AetherBlocks.SKYROOT_LEAF_PILE.asItem());
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> 0xB1FFCB), AetherBlocks.AETHER_GRASS_BLOCK.asItem(), AetherBlocks.AETHER_GRASS.asItem(), AetherBlocks.AETHER_TALL_GRASS.asItem(), AetherBlocks.AETHER_FERN.asItem(), AetherBlocks.AETHER_BUSH.asItem());
    }
}
