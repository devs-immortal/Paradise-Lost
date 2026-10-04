package net.id.paradise_lost.world.gen.noise;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.List;

import static net.id.paradise_lost.ModConstants.id;

public final class ParadiseLostNoise {
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_ELEVATION = of("generator/elevation");
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_EROSION = of("generator/erosion");
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_RIDGES = of("generator/ridges");
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_TEMPERATURE = of("generator/temperature");
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_VEGETATION = of("generator/vegetation");
    public static final ResourceKey<NormalNoise.NoiseParameters> GENERATOR_WEIRDNESS = of("generator/weirdness");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_CLIFFINESS = of("terrain/cliffiness");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_ELEVATION = of("terrain/elevation");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_FLAT = of("terrain/flat");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_ISLANDS = of("terrain/islands");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_OCEANS = of("terrain/oceans");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_RIBBING = of("terrain/ribbing");
    public static final ResourceKey<NormalNoise.NoiseParameters> TERRAIN_VOID = of("terrain/void");
    public static final ResourceKey<NormalNoise.NoiseParameters> TOPSOIL_DIRT_LAYER = of("topsoil/dirt_layer");
    public static final ResourceKey<NormalNoise.NoiseParameters> TOPSOIL_FOREST_NOISE = of("topsoil/forest_noise");
    public static final ResourceKey<NormalNoise.NoiseParameters> TOPSOIL_SHIELD_NOISE = of("topsoil/shield_noise");
    public static final ResourceKey<NormalNoise.NoiseParameters> TOPSOIL_THICKET_NOISE = of("topsoil/thicket_noise");

    private ParadiseLostNoise() {
    }

    private static ResourceKey<NormalNoise.NoiseParameters> of(String name) {
        return ResourceKey.create(Registries.NOISE, id(name));
    }

    public static void bootstrap(BootstrapContext<NormalNoise.NoiseParameters> context) {
        context.register(GENERATOR_ELEVATION, new NormalNoise.NoiseParameters(-8, List.of(1.0, 1.0, 0.0, 1.0, 1.0)));
        context.register(GENERATOR_EROSION, new NormalNoise.NoiseParameters(-9, List.of(1.0, 1.0, 1.0, 1.0)));
        context.register(GENERATOR_RIDGES, new NormalNoise.NoiseParameters(-6, List.of(1.0, 1.0, 1.0)));
        context.register(GENERATOR_TEMPERATURE, new NormalNoise.NoiseParameters(-9, List.of(1.5, 0.0, 1.0, 0.0, 0.0, 0.0)));
        context.register(GENERATOR_VEGETATION, new NormalNoise.NoiseParameters(-8, List.of(1.0, 1.0, 0.0, 0.0, 0.0, 0.0)));
        context.register(GENERATOR_WEIRDNESS, new NormalNoise.NoiseParameters(-10, List.of(1.0, 0.0, 0.0)));
        context.register(TERRAIN_CLIFFINESS, new NormalNoise.NoiseParameters(-13, List.of(-0.7)));
        context.register(TERRAIN_ELEVATION, new NormalNoise.NoiseParameters(-12, List.of(-1.4, -0.5, 1.5, 1.0, -3.5, -4.0, 1.0, 4.0, -4.0)));
        context.register(TERRAIN_FLAT, new NormalNoise.NoiseParameters(-13, List.of(-2.0, -1.0, 0.0, -0.5)));
        context.register(TERRAIN_ISLANDS, new NormalNoise.NoiseParameters(-7, List.of(3.0, 1.0, -3.0, 3.0)));
        context.register(TERRAIN_OCEANS, new NormalNoise.NoiseParameters(-9, List.of(-2.5)));
        context.register(TERRAIN_RIBBING, new NormalNoise.NoiseParameters(-6, List.of(0.5, -1.0, 0.0)));
        context.register(TERRAIN_VOID, new NormalNoise.NoiseParameters(-7, List.of(-16.0, -1.5, -3.0, 16.0)));
        context.register(TOPSOIL_DIRT_LAYER, new NormalNoise.NoiseParameters(-3, List.of(2.0, 1.0)));
        context.register(TOPSOIL_FOREST_NOISE, new NormalNoise.NoiseParameters(-4, List.of(0.0, 2.0)));
        context.register(TOPSOIL_SHIELD_NOISE, new NormalNoise.NoiseParameters(-4, List.of(0.1, -3.0)));
        context.register(TOPSOIL_THICKET_NOISE, new NormalNoise.NoiseParameters(-5, List.of(0.5, 2.0, 4.0)));
    }
}
