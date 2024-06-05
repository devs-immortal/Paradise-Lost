package net.id.paradiselost.util;

import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

// unused
public interface MapDimensionData {
    RegistryKey<World> getDimension();

    void setDimension(RegistryKey<World> dimension);
}
