package net.id.paradiselost.util;

import net.minecraft.world.RegistryWorldView;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class RegistryUtil {
    public static boolean dimensionMatches(@Nullable RegistryWorldView world, RegistryKey<DimensionType> type) {
        if (world == null) {
            return false;
        }

        DimensionType shatteredSky = world.getRegistryManager().get(Registry.DIMENSION_TYPE_KEY).get(type);
        return world.getDimension().equals(shatteredSky);
    }
}
