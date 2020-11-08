package com.aether.world.biome;

import com.aether.Aether;
import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.RuleTestType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.decorator.RangeDecoratorConfig;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import org.apache.logging.log4j.Level;

import java.util.Random;
import java.util.function.Predicate;

public class OreGeneratorProvider {
    public static ConfiguredFeature<?, ?> AmbrosiumVein = Feature.ORE
            .configure(new OreFeatureConfig(
                    new BlockMatchRuleTest(AetherBlocks.holystone),
                    AetherBlocks.ambrosium_ore.getDefaultState(),
                    5))
            .decorate(Decorator.RANGE.configure(new RangeDecoratorConfig(
                    0,
                    18,
                    26)))
            .spreadHorizontally()
            .repeat(20);

    public static void Init() {
        Aether.AETHER_LOG.log(Level.INFO, "Initializing OreProvider");
        RegistryKey<ConfiguredFeature<?, ?>> AmbrosiumVein_end = RegistryKey.of(Registry.CONFIGURED_FEATURE_WORLDGEN,
                new Identifier("the_aether", "ambrosiumvien_end"));
    }
}
