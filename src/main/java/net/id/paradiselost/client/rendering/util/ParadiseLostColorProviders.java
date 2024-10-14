package net.id.paradiselost.client.rendering.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.world.biome.FoliageColors;
import net.minecraft.world.biome.GrassColors;

import static net.id.paradiselost.blocks.ParadiseLostBlocks.*;

@Environment(EnvType.CLIENT)
public class ParadiseLostColorProviders {
    public static void initClient() {
        initBlocks();
        initItems();
    }

    private static void initBlocks() {
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor()), AUREL_WOODSTUFF.leaves(), AUREL_LEAF_PILE, SHAMROCK);
        ColorProviderRegistryImpl.BLOCK.register(((state, world, pos, tintIndex) -> world != null && pos != null ? BiomeColors.getGrassColor(world, pos) : GrassColors.getColor(0.5D, 1.0D)), HIGHLANDS_GRASS, GRASS, GRASS_FLOWERING, SHORT_GRASS, TALL_GRASS, FERN, BUSH, POTTED_FERN);
    }

    private static void initItems() {
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> 0xf1ff99), AUREL_WOODSTUFF.leaves().asItem(), AUREL_LEAF_PILE.asItem(), SHAMROCK.asItem());
        ColorProviderRegistryImpl.ITEM.register(((stack, tintIndex) -> tintIndex == 0 ? 0xa2dbc2 : -1), HIGHLANDS_GRASS.asItem(), GRASS.asItem(), GRASS_FLOWERING.asItem(), SHORT_GRASS.asItem(), TALL_GRASS.asItem(), FERN.asItem(), BUSH.asItem());
    }

}
