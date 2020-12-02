package com.aether.client.rendering.block;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;

public class AetherColorProvs {

    public static void initializeClient() {
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            float a = ((float) (world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()) / 0xFFFFFF);
            float b = (float) 0x00ff23 / 0xFFFFFF;
            return (int) ((1F - (2 * ((1F - a) * (1F - b)))) * 0xFFFFFF) + 0x555555;
        }), AetherBlocks.SKYROOT_LEAVES);
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) ->  world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), AetherBlocks.AETHER_GRASS);
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> 11665355), AetherBlocks.AETHER_GRASS.asItem());
        ColorProviderRegistry.ITEM.register(((stack, tintIndex) -> 0xbce632), AetherBlocks.SKYROOT_LEAVES.asItem());
    }
}
