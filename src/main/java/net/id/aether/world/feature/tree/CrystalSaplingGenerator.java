package net.id.aether.world.feature.tree;

import net.id.aether.world.feature.AetherConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class CrystalSaplingGenerator extends SaplingGenerator {
    @Override
    protected @Nullable ConfiguredFeature<TreeFeatureConfig, ?> getTreeFeature(Random random, boolean bl) {
        return AetherConfiguredFeatures.CRYSTAL_TREE;
    }
}
