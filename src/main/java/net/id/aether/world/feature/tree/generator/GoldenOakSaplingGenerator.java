package net.id.aether.world.feature.tree.generator;

import net.id.aether.world.feature.configured_features.AetherTreeConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.jetbrains.annotations.Nullable;

public class GoldenOakSaplingGenerator extends SaplingGenerator {
    @Override
    protected @Nullable RegistryEntry<? extends ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bl) {
        return AetherTreeConfiguredFeatures.GOLDEN_OAK_TREE;
    }
}