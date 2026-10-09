package net.id.paradise_lost.world.gen.noise;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CaveSurface;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostNoiseSettings {
    private ParadiseLostNoiseSettings() {}

    public static final ResourceKey<NoiseGeneratorSettings> NOISE = of("noise");

    public static ResourceKey<NoiseGeneratorSettings> of(String name) {
        return ResourceKey.create(Registries.NOISE_SETTINGS, id(name));
    }

    public static void bootstrap(BootstrapContext<NoiseGeneratorSettings> context) {
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        HolderGetter<NormalNoise.NoiseParameters> noiseParameters = context.lookup(Registries.NOISE);

        NoiseRouter router = new NoiseRouter(
                DensityFunctions.constant(0.0d),
                DensityFunctions.constant(-1.0d),
                DensityFunctions.constant(0.0d),
                DensityFunctions.constant(0.0d),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/temperature")))),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/vegetation")))),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/continents")))),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/erosion")))),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/depth")))),
                new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("generator/ridges")))),
                DensityFunctions.interpolated(DensityFunctions.yClampedGradient(60, -64, 0.0d, 1.0d)),
                DensityFunctions.interpolated(DensityFunctions.add(DensityFunctions.add(DensityFunctions.add(new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("bulk")))), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("spackle"))))), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("ridges"))))), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, id("hills")))))),
                DensityFunctions.constant(0.0d),
                DensityFunctions.constant(0.0d),
                DensityFunctions.constant(0.0d)
        );

        SurfaceRules.RuleSource surfaceRule = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.yBlockCheck(VerticalAnchor.absolute(60), 3), SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.steep()), SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("highlands_forest")), ResourceKey.create(Registries.BIOME, id("calcite_craglands"))), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/forest_noise")), 0.4d, 1.0d), SurfaceRules.state(BlockRegistry.COARSE_DIRT.get().defaultBlockState()))))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("highlands_thicket"))), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/thicket_noise")), 0.85d, 2.0d), SurfaceRules.state(BlockRegistry.LIVERWORT.get().defaultBlockState()))))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("highlands_grand_glade"))), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/thicket_noise")), 0.7d, 2.0d), SurfaceRules.state(BlockRegistry.LIVERWORT.get().defaultBlockState()))))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(3, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("highlands_shield"))), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/shield_noise")), 0.4d, 2.0d), SurfaceRules.state(BlockRegistry.FLOESTONE.get().defaultBlockState()))))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("highlands_shield"))), SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/shield_noise")), 0.3d, 0.45d), SurfaceRules.state(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState()))))),
                SurfaceRules.ifTrue(SurfaceRules.waterBlockCheck(-1, 0), SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("autumnal_tundra"))), SurfaceRules.state(BlockRegistry.FROZEN_GRASS.get().defaultBlockState())),
                SurfaceRules.state(BlockRegistry.HIGHLANDS_GRASS.get().defaultBlockState().setValue(SnowyDirtBlock.SNOWY, false)))),
                SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(2, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("autumnal_tundra"))), SurfaceRules.state(BlockRegistry.PERMAFROST.get().defaultBlockState())),
                SurfaceRules.state(BlockRegistry.DIRT.get().defaultBlockState()))),
                SurfaceRules.ifTrue(SurfaceRules.noiseCondition(ResourceKey.create(Registries.NOISE, id("topsoil/dirt_layer")), 0.0d, 1.0d), SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(3, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("autumnal_tundra"))), SurfaceRules.state(BlockRegistry.PERMAFROST.get().defaultBlockState())),
                SurfaceRules.state(BlockRegistry.DIRT.get().defaultBlockState()))))))),
                SurfaceRules.ifTrue(SurfaceRules.not(SurfaceRules.waterBlockCheck(-1, 0)), SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.stoneDepthCheck(0, false, 0, CaveSurface.FLOOR), SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.isBiome(ResourceKey.create(Registries.BIOME, id("autumnal_tundra"))), SurfaceRules.state(BlockRegistry.COBBLED_FLOESTONE.get().defaultBlockState())),
                SurfaceRules.state(BlockRegistry.DIRT.get().defaultBlockState())))))))))));

        List<Climate.ParameterPoint> spawnTarget = List.of(
                Climate.parameters(Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-0.11f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.point(0.0f), Climate.Parameter.span(-1.0f, -0.16f), 0.0f),
                Climate.parameters(Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.span(-0.11f, 1.0f), Climate.Parameter.span(-1.0f, 1.0f), Climate.Parameter.point(0.0f), Climate.Parameter.span(0.16f, 1.0f), 0.0f)
        );

        NoiseSettings noiseSettings = NoiseSettings.create(-32, 320, 1, 2);
        context.register(NOISE, new NoiseGeneratorSettings(noiseSettings, BlockRegistry.FLOESTONE.get().defaultBlockState(), Blocks.WATER.defaultBlockState(), router, surfaceRule, spawnTarget, -54, false, false, false, false));
    }
}
