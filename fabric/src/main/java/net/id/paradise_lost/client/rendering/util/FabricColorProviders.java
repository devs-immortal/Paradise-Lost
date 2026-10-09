package net.id.paradise_lost.client.rendering.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.world.item.Item;

@Environment(EnvType.CLIENT)
public final class FabricColorProviders {
    private FabricColorProviders() {}

    public static void initClient() {
        ParadiseLostColorProviders.registerBlocks((color, blocks) -> ColorProviderRegistry.BLOCK.register(color, blocks));
        ParadiseLostColorProviders.registerItems((color, blocks) -> {
            Item[] items = new Item[blocks.length];
            for (int i = 0; i < blocks.length; i++) {
                items[i] = blocks[i].asItem();
            }
            ColorProviderRegistry.ITEM.register(color, items);
        });
    }
}
