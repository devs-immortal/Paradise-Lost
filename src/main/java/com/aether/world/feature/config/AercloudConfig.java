package com.aether.world.feature.config;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class AercloudConfig implements FeatureConfiguration {

    public final BlockState state;
    public final boolean isFlat;
    public final int maxRadius;
    public final int y;

    public AercloudConfig(BlockState state, boolean isFlat, int segmentCount, int y) {
        this.state = state;
        this.isFlat = isFlat;
        this.maxRadius = segmentCount;
        this.y = y;
    }

    public BlockState getCloudState() {
        return this.state;
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
