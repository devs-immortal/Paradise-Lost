package net.id.paradise_lost.util;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class RegistryUtil {
    public static boolean dimensionMatches(@Nullable CommonLevelAccessor world, ResourceKey<DimensionType> type) {
        if (world == null) {
            return false;
        }

        DimensionType shatteredSky = world.registryAccess().registryOrThrow(Registries.DIMENSION_TYPE).get(type);
        return world.dimensionType().equals(shatteredSky);
    }
}
