package net.id.paradiselost.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.RegistryWorldView;
import net.minecraft.world.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class RegistryUtil {
    public static boolean dimensionMatches(@Nullable RegistryWorldView world, RegistryKey<DimensionType> type) {
        if (world == null) {
            return false;
        }

        DimensionType shatteredSky = world.getRegistryManager().get(RegistryKeys.DIMENSION_TYPE).get(type);
        return world.getDimension().equals(shatteredSky);
    }
}
