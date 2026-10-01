package net.id.paradise_lost.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public interface MapDimensionData {
    ResourceKey<Level> getDimension();

    void setDimension(ResourceKey<Level> dimension);
}
