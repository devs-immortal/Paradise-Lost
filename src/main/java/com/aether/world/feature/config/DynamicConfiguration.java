package com.aether.world.feature.config;

import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Optional;

public class DynamicConfiguration implements FeatureConfig {
    public final BlockState state;
    public final Optional<String> genTypeRaw;
    public final GeneratorType genType;

    public DynamicConfiguration(BlockState state, Optional<String> type) {
        this.state = state;
        this.genTypeRaw = type;

        final String genString = (genTypeRaw.orElse("normal")).toUpperCase();
        GeneratorType genType = GeneratorType.NORMAL;
        try {
            genType = GeneratorType.valueOf(genString);
        } catch (Exception ignored) {}
        this.genType = genType;
    }

    public BlockState getState() {
        return this.state;
    }

    public Optional<String> getGenString() {
        return genTypeRaw;
    }

    public GeneratorType getGenType() {
        return genType;
    }

    public enum GeneratorType {
        LEGACY, NORMAL
    }
}
