package com.aether.world.feature.tree;

import com.aether.world.feature.AetherConfiguredFeatures;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;

public class SkyrootSaplingGenerator extends AbstractTreeGrower {
    @Override
    protected @Nullable ConfiguredFeature<TreeConfiguration, ?> getConfiguredFeature(Random random, boolean bl) {
        return AetherConfiguredFeatures.SKYROOT_TREE;
    }
}