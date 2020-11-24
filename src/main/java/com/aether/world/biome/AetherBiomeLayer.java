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

    public static void setSeed(long seed) {
        if (perlinGen == null) {
            ChunkRandom sharedSeedRandom = new ChunkRandom(seed);
            perlinGen = new OctaveSimplexNoiseSampler(sharedSeedRandom, IntStream.rangeClosed(-1, 0));
        }
    }

    public int sample(LayerRandomnessSource noise, int x, int z) {
        return AetherBiomeSource.layersBiomeRegistry.getRawId(AetherBiomeSource.layersBiomeRegistry.get(AETHER_HIGHLANDS));
    }
}
