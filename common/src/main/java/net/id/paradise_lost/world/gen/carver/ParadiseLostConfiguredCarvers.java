package net.id.paradise_lost.world.gen.carver;

import net.id.paradise_lost.registry.BlockRegistry;
import net.id.paradise_lost.tag.ParadiseLostBlockTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.carver.CarverDebugSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.heightproviders.UniformHeight;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostConfiguredCarvers {
    private ParadiseLostConfiguredCarvers() {}

    public static final ResourceKey<ConfiguredWorldCarver<?>> BLUE_CLOUD = of("blue_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> COLD_CLOUD = of("cold_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> GOLDEN_CLOUD = of("golden_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> LARGE_BLUE_CLOUD = of("large_blue_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> LARGE_COLD_CLOUD = of("large_cold_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> LARGE_GOLDEN_CLOUD = of("large_golden_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> TINY_BLUE_CLOUD = of("tiny_blue_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> TINY_COLD_CLOUD = of("tiny_cold_cloud");
    public static final ResourceKey<ConfiguredWorldCarver<?>> TINY_GOLDEN_CLOUD = of("tiny_golden_cloud");

    public static ResourceKey<ConfiguredWorldCarver<?>> of(String name) {
        return ResourceKey.create(Registries.CONFIGURED_CARVER, id(name));
    }

    public static void bootstrap(BootstrapContext<ConfiguredWorldCarver<?>> context) {
        HolderSet.Named<Block> replaceable = context.lookup(Registries.BLOCK).getOrThrow(ParadiseLostBlockTags.CLOUD_CARVER_REPLACEABLES);
        context.register(BLUE_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.007f,
                        UniformHeight.of(VerticalAnchor.absolute(230), VerticalAnchor.absolute(310)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(32),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(5.48f, 6.75f),
                        UniformFloat.of(0.3f, 0.5f),
                        BlockStateProvider.simple(BlockRegistry.BLUE_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.233f),
                        UniformFloat.of(0.285f, 0.35f),
                        UniformInt.of(6, 7),
                        ConstantFloat.of(0.6f),
                        ConstantInt.of(16),
                        ConstantFloat.of(1.0f)
                )));
        context.register(COLD_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.0105f,
                        UniformHeight.of(VerticalAnchor.aboveBottom(4), VerticalAnchor.absolute(112)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(0),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(0.3f, 0.75f),
                        UniformFloat.of(0.36f, 0.6f),
                        BlockStateProvider.simple(BlockRegistry.COLD_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.15f),
                        UniformFloat.of(0.285f, 0.45f),
                        UniformInt.of(3, 4),
                        ConstantFloat.of(2.0f),
                        ConstantInt.of(3),
                        ConstantFloat.of(0.25f)
                )));
        context.register(GOLDEN_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.0085f,
                        UniformHeight.of(VerticalAnchor.aboveBottom(14), VerticalAnchor.absolute(68)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(32),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(0.5f, 1.25f),
                        UniformFloat.of(0.6f, 1.0f),
                        BlockStateProvider.simple(BlockRegistry.GOLDEN_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.225f),
                        UniformFloat.of(0.35f, 0.5f),
                        UniformInt.of(2, 5),
                        ConstantFloat.of(1.5f),
                        ConstantInt.of(3),
                        ConstantFloat.of(0.35f)
                )));
        context.register(LARGE_BLUE_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.001f,
                        UniformHeight.of(VerticalAnchor.absolute(230), VerticalAnchor.absolute(310)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(32),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(5.48f, 6.75f),
                        UniformFloat.of(0.3f, 0.5f),
                        BlockStateProvider.simple(BlockRegistry.BLUE_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.233f),
                        UniformFloat.of(0.285f, 0.35f),
                        UniformInt.of(6, 7),
                        ConstantFloat.of(0.6f),
                        ConstantInt.of(16),
                        ConstantFloat.of(1.0f)
                )));
        context.register(LARGE_COLD_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.0003f,
                        UniformHeight.of(VerticalAnchor.absolute(260), VerticalAnchor.absolute(340)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(32),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(1.0f, 2.15f),
                        UniformFloat.of(0.6f, 1.0f),
                        BlockStateProvider.simple(BlockRegistry.COLD_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.1f),
                        UniformFloat.of(0.285f, 0.45f),
                        UniformInt.of(8, 9),
                        ConstantFloat.of(0.3f),
                        ConstantInt.of(16),
                        ConstantFloat.of(1.0f)
                )));
        context.register(LARGE_GOLDEN_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.0006f,
                        UniformHeight.of(VerticalAnchor.absolute(290), VerticalAnchor.absolute(360)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(32),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(1.6f, 2.0f),
                        UniformFloat.of(1.5f, 1.85f),
                        BlockStateProvider.simple(BlockRegistry.GOLDEN_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.322f),
                        UniformFloat.of(0.585f, 0.75f),
                        UniformInt.of(7, 9),
                        ConstantFloat.of(0.4f),
                        ConstantInt.of(16),
                        ConstantFloat.of(1.0f)
                )));
        context.register(TINY_BLUE_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.009f,
                        UniformHeight.of(VerticalAnchor.aboveBottom(100), VerticalAnchor.absolute(260)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(0),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(0.3f, 0.75f),
                        UniformFloat.of(0.36f, 0.6f),
                        BlockStateProvider.simple(BlockRegistry.BLUE_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.15f),
                        UniformFloat.of(0.785f, 1.25f),
                        UniformInt.of(1, 2),
                        ConstantFloat.of(3.0f),
                        ConstantInt.of(1),
                        ConstantFloat.of(0.065f)
                )));
        context.register(TINY_COLD_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.01f,
                        UniformHeight.of(VerticalAnchor.aboveBottom(100), VerticalAnchor.absolute(260)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(0),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(0.3f, 0.75f),
                        UniformFloat.of(0.36f, 0.6f),
                        BlockStateProvider.simple(BlockRegistry.COLD_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.15f),
                        UniformFloat.of(0.785f, 1.25f),
                        UniformInt.of(1, 2),
                        ConstantFloat.of(3.0f),
                        ConstantInt.of(1),
                        ConstantFloat.of(0.065f)
                )));
        context.register(TINY_GOLDEN_CLOUD, new ConfiguredWorldCarver<>(
                ParadiseLostCarvers.CLOUD_CARVER,
                new CloudCarverConfig(
                        0.00875f,
                        UniformHeight.of(VerticalAnchor.aboveBottom(100), VerticalAnchor.absolute(260)),
                        UniformFloat.of(0.5f, 1.1f),
                        VerticalAnchor.aboveBottom(0),
                        CarverDebugSettings.DEFAULT,
                        replaceable,
                        UniformFloat.of(0.3f, 0.75f),
                        UniformFloat.of(0.36f, 0.6f),
                        BlockStateProvider.simple(BlockRegistry.GOLDEN_CLOUD.get().defaultBlockState()),
                        ConstantFloat.of(0.15f),
                        UniformFloat.of(0.785f, 1.25f),
                        UniformInt.of(1, 2),
                        ConstantFloat.of(3.0f),
                        ConstantInt.of(1),
                        ConstantFloat.of(0.065f)
                )));
    }
}
