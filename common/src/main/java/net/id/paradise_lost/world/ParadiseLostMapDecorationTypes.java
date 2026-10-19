package net.id.paradise_lost.world;

import static net.id.paradise_lost.ModConstants.locate;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public class ParadiseLostMapDecorationTypes {
    private static final RegistrationProvider<MapDecorationType> MAP_DECORATION_TYPES =
            RegistrationProvider.get(Registries.MAP_DECORATION_TYPE, ModConstants.MOD_ID);

    public static void init() {
    }

    public static final Holder<MapDecorationType> VAULT = register(
            "vault", "vault", true, MapColor.TERRACOTTA_ORANGE.col, false, true
    );

    public static final Holder<MapDecorationType> PALACE = register(
            "palace", "palace", true, MapColor.COLOR_CYAN.col, false, true
    );

    private static Holder<MapDecorationType> register(
            String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement
    ) {
        MapDecorationType mapDecorationType = new MapDecorationType(locate(assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount);
        MAP_DECORATION_TYPES.register(id, () -> mapDecorationType);
        return Holder.direct(mapDecorationType);
    }
}
