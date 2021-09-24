package net.id.aether.util;

import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

// Never used
public interface MapDimensionData {
    RegistryKey<World> getDimension();

    void setDimension(RegistryKey<World> dimension);
}