package net.id.paradiselost.world;

import net.minecraft.block.MapColor;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

import static net.id.paradiselost.ParadiseLost.locate;

public class ParadiseLostMapDecorationTypes {

    public static void init() {
        // no-op
    }

    public static final RegistryEntry<MapDecorationType> VAULT = register(
            "vault", "vault", true, MapColor.TERRACOTTA_ORANGE.color, false, true
    );

    private static RegistryEntry<MapDecorationType> register(
            String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement
    ) {
        RegistryKey<MapDecorationType> registryKey = RegistryKey.of(RegistryKeys.MAP_DECORATION_TYPE, locate(id));
        MapDecorationType mapDecorationType = new MapDecorationType(locate(assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount);
        return Registry.registerReference(Registries.MAP_DECORATION_TYPE, registryKey, mapDecorationType);
    }
}
