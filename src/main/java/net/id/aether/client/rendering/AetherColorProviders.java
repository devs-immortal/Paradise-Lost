package net.id.aether.client.rendering;

import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.color.world.GrassColors;

public class AetherColorProviders {
    public static void initClient() {
        initBlocks();
        initItems();
    }

    private static void initBlocks() {
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), AetherBlocks.SKYROOT_LEAVES, AetherBlocks.SKYROOT_LEAF_PILE);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_GRASS, AetherBlocks.AETHER_TALL_GRASS, AetherBlocks.AETHER_FERN, AetherBlocks.AETHER_BUSH);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : -1), AetherBlocks.HONEY_NETTLE, AetherBlocks.WEEPING_CLOUDBURST);
    }

    private static void initItems() {
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> 0xbce632), AetherBlocks.SKYROOT_LEAVES.asItem(), AetherBlocks.SKYROOT_LEAF_PILE.asItem());
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> 0xB1FFCB), AetherBlocks.AETHER_GRASS_BLOCK.asItem(), AetherBlocks.AETHER_GRASS.asItem(), AetherBlocks.AETHER_TALL_GRASS.asItem(), AetherBlocks.AETHER_FERN.asItem(), AetherBlocks.AETHER_BUSH.asItem());
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> 0x599CFF), AetherBlocks.BOREAL_WISTERIA_HANGER.asItem(), AetherBlocks.BOREAL_WISTERIA_LEAVES.asItem());
    }
}
