package com.aether.util;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public interface MapDimensionData {
    RegistryKey<World> getDimension();

    void setDimension(RegistryKey<World> dimension);

}
