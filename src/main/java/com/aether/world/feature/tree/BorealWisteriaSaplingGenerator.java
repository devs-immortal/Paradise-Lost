package com.aether.world.feature.tree;

import com.aether.world.feature.AetherConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class BorealWisteriaSaplingGenerator extends SaplingGenerator {
    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> createTreeFeature(Random random, boolean bl) {
        return random.nextInt(20) == 0 ? AetherConfiguredFeatures.BOREAL_WISTERIA_TREE : AetherConfiguredFeatures.FANCY_BOREAL_WISTERIA_TREE;
    }
}