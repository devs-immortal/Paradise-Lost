package net.id.paradise_lost.world;

import static net.id.paradise_lost.ModConstants.id;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public class ParadiseLostMapDecorationTypes {
    private static final RegistrationProvider<MapDecorationType> MAP_DECORATION_TYPES =
            RegistrationProvider.get(Registries.MAP_DECORATION_TYPE, ModConstants.MODID);

    public static void init() {
    }

    public static final Holder<MapDecorationType> VAULT = register(
            "vault", "vault", true, MapColor.TERRACOTTA_ORANGE.col, false, true
    );

    private static Holder<MapDecorationType> register(
            String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement
    ) {
        MapDecorationType mapDecorationType = new MapDecorationType(id(assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount);
        MAP_DECORATION_TYPES.register(id, () -> mapDecorationType);
        return Holder.direct(mapDecorationType);
    }
}
