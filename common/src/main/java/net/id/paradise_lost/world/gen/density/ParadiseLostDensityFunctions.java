package net.id.paradise_lost.world.gen.density;

import net.id.paradise_lost.world.gen.noise.ParadiseLostNoise;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostDensityFunctions {
    public static final ResourceKey<DensityFunction> BULK = of("bulk");
    public static final ResourceKey<DensityFunction> GENERATOR_CONTINENTS = of("generator/continents");
    public static final ResourceKey<DensityFunction> GENERATOR_DEPTH = of("generator/depth");
    public static final ResourceKey<DensityFunction> GENERATOR_EROSION = of("generator/erosion");
    public static final ResourceKey<DensityFunction> GENERATOR_RIDGES = of("generator/ridges");
    public static final ResourceKey<DensityFunction> GENERATOR_TEMPERATURE = of("generator/temperature");
    public static final ResourceKey<DensityFunction> GENERATOR_VEGETATION = of("generator/vegetation");
    public static final ResourceKey<DensityFunction> HILLS = of("hills");
    public static final ResourceKey<DensityFunction> RIDGES = of("ridges");
    public static final ResourceKey<DensityFunction> SPACKLE = of("spackle");

    private ParadiseLostDensityFunctions() {
    }

    private static ResourceKey<DensityFunction> of(String name) {
        return ResourceKey.create(Registries.DENSITY_FUNCTION, id(name));
    }

    private static final double CLIMATE_AMPLITUDE = 1.4;

    private static DensityFunction amplify(DensityFunction source) {
        return DensityFunctions.mul(DensityFunctions.constant(CLIMATE_AMPLITUDE), source);
    }

    public static void bootstrap(BootstrapContext<DensityFunction> context) {
        HolderGetter<NormalNoise.NoiseParameters> noises = context.lookup(Registries.NOISE);
        HolderGetter<DensityFunction> densityFunctions = context.lookup(Registries.DENSITY_FUNCTION);
        context.register(BULK, DensityFunctions.min(DensityFunctions.max(DensityFunctions.max(DensityFunctions.yClampedGradient(320, 70, -100.0, -1.0), DensityFunctions.add(DensityFunctions.yClampedGradient(70, -200, 0.0, -3.0), DensityFunctions.mul(DensityFunctions.constant(-0.5), DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_EROSION), 0.5, 1.0))))), DensityFunctions.constant(-1.0)), DensityFunctions.constant(0.5)));
        context.register(GENERATOR_CONTINENTS, DensityFunctions.flatCache(amplify(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_ELEVATION), 1.0, 0.0))));
        context.register(GENERATOR_DEPTH, DensityFunctions.add(DensityFunctions.yClampedGradient(-64, 320, 1.5, -1.5), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("minecraft:overworld/offset"))))));
        context.register(GENERATOR_EROSION, amplify(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_EROSION), 1.0, 0.0)));
        context.register(GENERATOR_RIDGES, DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_WEIRDNESS), 1.0, 0.0)));
        context.register(GENERATOR_TEMPERATURE, amplify(DensityFunctions.shiftedNoise2d(new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("minecraft:shift_x")))), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("minecraft:shift_z")))), 0.28, noises.getOrThrow(ParadiseLostNoise.GENERATOR_TEMPERATURE))));
        context.register(GENERATOR_VEGETATION, amplify(DensityFunctions.shiftedNoise2d(new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("minecraft:shift_x")))), new DensityFunctions.HolderHolder(densityFunctions.getOrThrow(ResourceKey.create(Registries.DENSITY_FUNCTION, ResourceLocation.parse("minecraft:shift_z")))), 0.33, noises.getOrThrow(ParadiseLostNoise.GENERATOR_VEGETATION))));
        context.register(HILLS, DensityFunctions.mul(DensityFunctions.mul(DensityFunctions.cache2d(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_ELEVATION), 1.0, 1.0)), DensityFunctions.add(DensityFunctions.mul(DensityFunctions.yClampedGradient(130, 320, 1.0, 0.0), DensityFunctions.yClampedGradient(130, 320, 1.0, 0.0)), DensityFunctions.mul(DensityFunctions.yClampedGradient(0, 120, 0.0, 1.0), DensityFunctions.yClampedGradient(0, 120, 0.0, 1.0)))), DensityFunctions.constant(0.5)));
        context.register(RIDGES, DensityFunctions.mul(DensityFunctions.constant(0.6), DensityFunctions.mul(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_RIDGES), 1.0, 1.0), DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_RIDGES), 1.0, 1.0))));
        context.register(SPACKLE, DensityFunctions.add(DensityFunctions.yClampedGradient(-32, 16, -1.0, 0.0), DensityFunctions.add(DensityFunctions.yClampedGradient(120, 212, 0.0, -2.0), DensityFunctions.mul(DensityFunctions.constant(-3.0), DensityFunctions.add(DensityFunctions.constant(0.0), DensityFunctions.mul(DensityFunctions.mul(DensityFunctions.max(DensityFunctions.add(DensityFunctions.constant(0.2), DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_RIDGES), 1.0, 1.0))), DensityFunctions.constant(-0.3)), DensityFunctions.max(DensityFunctions.add(DensityFunctions.constant(0.2), DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_RIDGES), 1.0, 1.0))), DensityFunctions.constant(-0.3))), DensityFunctions.max(DensityFunctions.add(DensityFunctions.constant(0.2), DensityFunctions.flatCache(DensityFunctions.noise(noises.getOrThrow(ParadiseLostNoise.GENERATOR_RIDGES), 1.0, 1.0))), DensityFunctions.constant(-0.3))))))));
    }
}
