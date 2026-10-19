package net.id.paradise_lost.util;

import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.Potions;
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

    public static void addBrewingRecipes(PotionBrewing.Builder builder) {
        builder.addMix(Potions.HEALING, ItemRegistry.POPOM_JELLY.get(), ItemRegistry.HEALTH_BOOST_POTION);
        builder.addMix(ItemRegistry.HEALTH_BOOST_POTION, Items.REDSTONE, ItemRegistry.LONG_HEALTH_BOOST_POTION);
        builder.addMix(ItemRegistry.HEALTH_BOOST_POTION, Items.GLOWSTONE_DUST, ItemRegistry.STRONG_HEALTH_BOOST_POTION);
    }
}
