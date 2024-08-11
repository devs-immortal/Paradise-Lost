package net.id.paradiselost.world.feature.tree.generator;

import net.id.paradiselost.world.feature.configured_features.ParadiseLostTreeConfiguredFeatures;
import net.minecraft.block.sapling.SaplingGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class MotherAurelSaplingGenerator extends SaplingGenerator {
    @Override
    protected RegistryKey<ConfiguredFeature<?, ?>> getTreeFeature(Random random, boolean bees) {
        return ParadiseLostTreeConfiguredFeatures.MOTHER_AUREL_TREE;
    }
}
