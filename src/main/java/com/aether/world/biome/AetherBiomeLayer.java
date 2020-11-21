package com.aether.world.biome;

import com.aether.Aether;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.noise.OctaveSimplexNoiseSampler;
import net.minecraft.world.biome.layer.type.InitLayer;
import net.minecraft.world.biome.layer.util.LayerRandomnessSource;
import net.minecraft.world.gen.ChunkRandom;

import java.util.stream.IntStream;

public enum AetherBiomeLayer implements InitLayer {
    INSTANCE;

    private static final Identifier AETHER_HIGHLANDS = Aether.locate("aether_highlands");

    private static OctaveSimplexNoiseSampler perlinGen;
//	private double max = -100;
//	private double min = 100;

    public static void setSeed(long seed) {
        if (perlinGen == null) {
            ChunkRandom sharedSeedRandom = new ChunkRandom(seed);
            perlinGen = new OctaveSimplexNoiseSampler(sharedSeedRandom, IntStream.rangeClosed(-1, 0));
        }
    }

    public int sample(LayerRandomnessSource noise, int x, int z) {
//        double perlinNoise = perlinGen.sample((double) x * 0.1D, (double) z * 0.0001D, false);
//
//		max = Math.max(max, perlinNoise);
//		min = Math.min(min, perlinNoise);
//		Aether.LOGGER.log(Level.INFO, "Max: " + max +", Min: "+min + ", perlin: "+perlinNoise);

        /*if (Math.abs(perlinNoise) % 0.1D < 0.07D) {
            return AetherBiomeSource.layersBiomeRegistry.getRawId(AetherBiomeSource.layersBiomeRegistry.get(aab));
        }
        else {
            return AetherBiomeSource.layersBiomeRegistry.getRawId(AetherBiomeSource.layersBiomeRegistry.get(bba));
        }*/
        return AetherBiomeSource.layersBiomeRegistry.getRawId(AetherBiomeSource.layersBiomeRegistry.get(AETHER_HIGHLANDS));
    }
}
