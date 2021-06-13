package com.aether.world.feature.config;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Optional;

public class AercloudConfig extends DynamicConfiguration {
    public final boolean isFlat;
    public final int maxRadius;
    public final int y;

    public AercloudConfig(BlockState state, Optional<String> type, boolean isFlat, int segmentCount, int y) {
        super(state, type);
        this.isFlat = isFlat;
        this.maxRadius = segmentCount;
        this.y = y;
    }

    public int getY() {
        return this.y;
    }

    public int cloudModifier() {
        return this.isFlat ? 3 : 1;
    }

    public int maxSegments() {
        return this.maxRadius;
    }

    public boolean isFlat() {
        return this.isFlat;
    }
}
