package net.id.aether.client.rendering.util;

import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.blocks.util.DynamicColorBlock;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;
import net.minecraft.util.registry.Registry;

public class AetherColorProviders {
    public static void initClient() {
        initBlocks();
        initItems();
        initDynamicColorBlocks();
    }

    private static void initBlocks() {
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), AetherBlocks.SKYROOT_LEAVES, AetherBlocks.SKYROOT_LEAF_PILE);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_GRASS, AetherBlocks.AETHER_GRASS_FLOWERING, AetherBlocks.AETHER_SHORT_GRASS, AetherBlocks.AETHER_TALL_GRASS, AetherBlocks.AETHER_FERN, AetherBlocks.AETHER_BUSH);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : -1), AetherBlocks.HONEY_NETTLE, AetherBlocks.WEEPING_CLOUDBURST);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getWaterColor(world, pos) : -1), AetherBlocks.SPRING_WATER);
    }

    private static void initItems() {
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> 0xbce632), AetherBlocks.SKYROOT_LEAVES.asItem(), AetherBlocks.SKYROOT_LEAF_PILE.asItem());
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> tintIndex == 0 ? 0xB1FFCB : -1), AetherBlocks.AETHER_GRASS_BLOCK.asItem(), AetherBlocks.AETHER_GRASS.asItem(), AetherBlocks.AETHER_GRASS_FLOWERING.asItem(), AetherBlocks.AETHER_SHORT_GRASS.asItem(), AetherBlocks.AETHER_TALL_GRASS.asItem(), AetherBlocks.AETHER_FERN.asItem(), AetherBlocks.AETHER_BUSH.asItem());
    }

    private static void initDynamicColorBlocks() {
        // Ideally we shouldn't go through the entire block registry, but it's almost instantaneous anyway
        Registry.BLOCK.stream()
                .filter(block -> block instanceof DynamicColorBlock)
                .forEach(block -> {
                    DynamicColorBlock dynamic = (DynamicColorBlock) block;
                    ColorProviderRegistryImpl.BLOCK.register(dynamic.getBlockColorProvider(), block);
                    ColorProviderRegistryImpl.ITEM.register(dynamic.getBlockItemColorProvider(), block.asItem());
                });
    }
}
