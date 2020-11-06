package com.aether.world.biome;

import java.util.Map;
import java.util.function.LongFunction;
import java.util.stream.Collectors;

import com.aether.Aether;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.SharedConstants;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BuiltinBiomes;
import net.minecraft.world.biome.layer.ScaleLayer;
import net.minecraft.world.biome.layer.type.ParentedLayer;
import net.minecraft.world.biome.layer.util.CachingLayerContext;
import net.minecraft.world.biome.layer.util.CachingLayerSampler;
import net.minecraft.world.biome.layer.util.LayerFactory;
import net.minecraft.world.biome.layer.util.LayerSampleContext;
import net.minecraft.world.biome.layer.util.LayerSampler;
import net.minecraft.world.biome.source.BiomeLayerSampler;
import net.minecraft.world.biome.source.BiomeSource;

public class AetherBiomeProvider extends BiomeSource {
    public static final Codec<AetherBiomeProvider> CODEC =
            RecordCodecBuilder.create((instance) -> instance.group(
                    RegistryLookupCodec.of(Registry.BIOME_KEY).forGetter((biomeSource) -> biomeSource.BIOME_REGISTRY))
            .apply(instance, instance.stable(AetherBiomeProvider::new)));

    private final BiomeLayerSampler BIOME_SAMPLER;
    private final Registry<Biome> BIOME_REGISTRY;
    public static Registry<Biome> layersBiomeRegistry;

    public AetherBiomeProvider(Registry<Biome> biomeRegistry) {
        // FIXME: Need World Seed Here
        this(0, biomeRegistry);
    }

    public AetherBiomeProvider(long seed, Registry<Biome> biomeRegistry) {
        super(biomeRegistry.getEntries().stream()
                .filter(entry -> entry.getKey().getValue().getNamespace().equals(Aether.MODID))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList()));

        //AetherBiomeLayer.setSeed(seed);
        this.BIOME_REGISTRY = biomeRegistry;
        AetherBiomeProvider.layersBiomeRegistry = biomeRegistry;
        this.BIOME_SAMPLER = null;
        //this.BIOME_SAMPLER = buildWorldProcedure(seed);
    }

    public static void registerBiomeProvider() {
        Registry.register(Registry.BIOME_SOURCE, new Identifier(Aether.MODID, "biome_source"), AetherBiomeProvider.CODEC);
    }

    @Override
    protected Codec<? extends BiomeSource> getCodec() {
        return CODEC;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public BiomeSource withSeed(long seed) {
        return new AetherBiomeProvider(seed, this.BIOME_REGISTRY);
    }

    public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> stack(long seed, ParentedLayer parent, LayerFactory<T> incomingArea, int count, LongFunction<C> contextFactory) {
        LayerFactory<T> LayerFactory = incomingArea;

        for (int i = 0; i < count; ++i) {
            LayerFactory = parent.create(contextFactory.apply(seed + (long) i), LayerFactory);
        }

        return LayerFactory;
    }


    /*public static BiomeLayerSampler buildWorldProcedure(long seed) {
        LayerFactory<CachingLayerSampler> layerFactory = build((salt) ->
                new CachingLayerContext(25, seed, salt));
        return new BiomeLayerSampler(layerFactory);
    }*/


    /*public static <T extends LayerSampler, C extends LayerSampleContext<T>> LayerFactory<T> build(LongFunction<C> contextFactory) {
        LayerFactory<T> layer = AetherBiomeLayer.INSTANCE.create(contextFactory.apply(200L));
        layer = AetherBiomePillarLayer.INSTANCE.create(contextFactory.apply(1008L), layer);
        layer = AetherBiomeScalePillarLayer.INSTANCE.create(contextFactory.apply(1055L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2003L), layer);
        layer = ScaleLayer.FUZZY.create(contextFactory.apply(2523L), layer);
        return layer;
    }*/

    @Override
    public Biome getBiomeForNoiseGen(int x, int y, int z) {
        return this.sample(this.BIOME_REGISTRY, x, z);
    }

    public Biome sample(Registry<Biome> registry, int i, int j) {
        /*int k = ((BiomeLayerSamplerAccessor)this.BIOME_SAMPLER).getSampler().sample(i, j);
        Biome biome = registry.get(k);
        if (biome == null) {
            if (SharedConstants.isDevelopment) {
                throw Util.throwOrPause(new IllegalStateException("Unknown biome id: " + k));
            } else {
                return registry.get(BuiltinBiomes.fromRawId(0));
            }
        } else {
            return biome;
        }*/
        return BuiltinBiomes.PLAINS;
    }
}
