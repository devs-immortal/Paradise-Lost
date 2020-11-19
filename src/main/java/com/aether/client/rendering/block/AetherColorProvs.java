package com.aether.client.rendering.block;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.util.registry.Registry;

public class AetherColorProvs {

    public static void initializeClient() {
        ColorProviderRegistry.BLOCK.register(((state, world, pos, tintIndex) -> {
            float a = ( (float) (world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()) / 0xFFFFFF);
            float b = (float) 0x00ff23 / 0xFFFFFF;
            return (int) ((1F - (2 * ((1F - a) * (1F - b)))) * 0xFFFFFF) + 0x555555;
        }), AetherBlocks.SKYROOT_LEAVES);
    }
}
