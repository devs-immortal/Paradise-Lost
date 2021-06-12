package com.aether.util;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.CommonLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import org.jetbrains.annotations.Nullable;

public class RegistryUtil {
	public static boolean dimensionMatches(@Nullable CommonLevelAccessor world, ResourceKey<DimensionType> type) {
		if (world == null) return false;

		DimensionType shatteredSky = world.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).get(type);
		return world.dimensionType().equalTo(shatteredSky);
	}
}
