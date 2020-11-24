package com.aether.world.biome;

import com.aether.Aether;
import com.aether.callback.ServerChunkManagerCallback;
import com.aether.mixin.render.BiomeLayerSamplerAccessor;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.*;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

public class AetherBiomeSource extends BiomeSource {
    public static Registry<Biome> layersBiomeRegistry;
    public static final Codec<AetherBiomeSource> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter(source -> source.biomeRegistry),
            Codec.LONG.fieldOf("seed").stable().forGetter(source -> source.seed)
    ).apply(instance, instance.stable(AetherBiomeSource::new)));
    private final Registry<Biome> biomeRegistry;
    private BiomeLayerSampler biomeSampler;
    private long seed;
    private boolean isSetup = false;

    public AetherBiomeSource(Registry<Biome> biomeRegistry) {
        this(biomeRegistry, 0L);
    }

    public AetherBiomeSource(Registry<Biome> biomeRegistry, long worldSeed) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getValue().getNamespace().equals(Aether.MOD_ID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        this.biomeRegistry = biomeRegistry;
        AetherBiomeSource.layersBiomeRegistry = biomeRegistry;
        this.seed = worldSeed;

        if (this.seed == 0L) {
            Aether.LOG.warn("AetherBiomeSource has 0'd seed, using workaround...");
            ServerChunkManagerCallback.EVENT.register(manager -> {
                if (!isSetup && this.seed == 0L) {
                    this.seed = ((ServerWorld) manager.getWorld()).getSeed();

                    Aether.LOG.info("Using Seed for AetherBiomeSource -> " + this.seed);
                    AetherBiomeLayer.setSeed(this.seed);
                    this.biomeSampler = buildWorldProcedure(this.seed);

                    isSetup = true;
                }
            });
        } else {
            AetherBiomeLayer.setSeed(this.seed);
            this.biomeSampler = buildWorldProcedure(this.seed);
        }
    }

    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, Aether.locate("biome_source"), AetherBiomeSource.CODEC);
    }

    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i)
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);

        return LayerFactory;
    }

    public static BiomeLayerSampler buildWorldProcedure(long seed) {
        LayerFactory<CachingLayerSampler> layerFactory = build((salt) ->
                new CachingLayerContext(25, seed, salt));
        return new BiomeLayerSampler(layerFactory);
    }

    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextFactory) {
        return AetherBiomeLayer.INSTANCE.create(contextFactory.apply(1L));
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new AetherBiomeSource(this.biomeRegistry, seed);
    }

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.sample(this.biomeRegistry, x, z);
    }

    public Biome sample(Registry<Biome> registry, int i, int j) {
        int k = ((BiomeLayerSamplerAccessor) this.biomeSampler).getSampler().sample(i, j);
        Biome biome = registry.get(k);
        if (biome == null) {
            if (SharedConstants.isDevelopment)
                throw Util.throwOrPause(new IllegalStateException("Unknown biome id: " + k));
            else return registry.get(BuiltinBiomes.fromRawId(0));
        } else {
            return biome;
        }
    }
}
