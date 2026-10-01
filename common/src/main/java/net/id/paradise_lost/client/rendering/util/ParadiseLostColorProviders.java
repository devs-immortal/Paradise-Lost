package net.id.paradise_lost.client.rendering.util;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public final class ParadiseLostColorProviders {
    private ParadiseLostColorProviders() {}

    public static final BlockColor FOLIAGE = (state, level, pos, tintIndex) ->
            level != null && pos != null ? BiomeColors.getAverageFoliageColor(level, pos) : FoliageColor.getDefaultColor();

    public static final BlockColor GRASS = (state, level, pos, tintIndex) ->
            level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.get(0.5D, 1.0D);

    public static final ItemColor FOLIAGE_ITEM = (stack, tintIndex) -> 0xf1ff99;

    public static final ItemColor GRASS_ITEM = (stack, tintIndex) -> tintIndex == 0 ? 0xa2dbc2 : -1;

    public static void registerBlocks(BiConsumer<BlockColor, Block[]> registrar) {
        Block leaves = BlockRegistry.AUREL_WOODSTUFF.leaves().get();
        registrar.accept(FOLIAGE, new Block[]{
                leaves,
                BlockRegistry.AUREL_LEAF_PILE.get(),
                BlockRegistry.SHAMROCK.get()
        });
        registrar.accept(GRASS, new Block[]{
                BlockRegistry.HIGHLANDS_GRASS.get(),
                BlockRegistry.GRASS.get(),
                BlockRegistry.GRASS_FLOWERING.get(),
                BlockRegistry.SHORT_GRASS.get(),
                BlockRegistry.TALL_GRASS.get(),
                BlockRegistry.FERN.get(),
                BlockRegistry.BUSH.get(),
                BlockRegistry.POTTED_FERN.get()
        });
    }

    public static void registerItems(BiConsumer<ItemColor, Block[]> registrar) {
        Block leaves = BlockRegistry.AUREL_WOODSTUFF.leaves().get();
        registrar.accept(FOLIAGE_ITEM, new Block[]{
                leaves,
                BlockRegistry.AUREL_LEAF_PILE.get(),
                BlockRegistry.SHAMROCK.get()
        });
        registrar.accept(GRASS_ITEM, new Block[]{
                BlockRegistry.HIGHLANDS_GRASS.get(),
                BlockRegistry.GRASS.get(),
                BlockRegistry.GRASS_FLOWERING.get(),
                BlockRegistry.SHORT_GRASS.get(),
                BlockRegistry.TALL_GRASS.get(),
                BlockRegistry.FERN.get(),
                BlockRegistry.BUSH.get()
        });
    }
}
