package net.id.paradise_lost.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.api.MoaAPI.MoaRace;
import net.id.paradise_lost.registration.registries.DatapackRegistry;
import net.id.paradise_lost.world.dimension.ParadiseLostBiomes;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandom;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public final class MoaSpawnRegistry {

    public static final DatapackRegistry<SpawnEntry> REGISTRY =
            DatapackRegistry.<SpawnEntry>builder(ResourceLocation.fromNamespaceAndPath(ModConstants.MODID, "moa_spawn"))
                    .withElementCodec(SpawnEntry.CODEC)
                    .withNetworkCodec(SpawnEntry.CODEC)
                    .withBootstrap(MoaSpawnRegistry::bootstrap)
                    .build();

    public static final ResourceLocation DEFAULT_ID = ModConstants.id("default");

    private static final Set<ResourceLocation> WARNED_MISSING_RACES = ConcurrentHashMap.newKeySet();

    private MoaSpawnRegistry() {
    }

    public static void init() {
    }

    private static void bootstrap(BootstrapContext<SpawnEntry> context) {
        table(context, ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY,
                weighted(MoaRaceRegistry.HIGHLANDS_BLUE, 50),
                weighted(MoaRaceRegistry.HIGHLANDS_CYAN, 30),
                weighted(MoaRaceRegistry.MINTGRASS, 35));

        table(context, ParadiseLostBiomes.HIGHLANDS_FOREST_KEY,
                weighted(MoaRaceRegistry.HIGHLANDS_BLUE, 45),
                weighted(MoaRaceRegistry.GOLDENROD, 25),
                weighted(MoaRaceRegistry.TANGERINE, 15));

        table(context, ParadiseLostBiomes.HIGHLANDS_SHIELD_KEY,
                weighted(MoaRaceRegistry.GOLDENROD, 10),
                weighted(MoaRaceRegistry.FOXTROT, 4));

        table(context, ParadiseLostBiomes.HIGHLANDS_THICKET_KEY,
                weighted(MoaRaceRegistry.MINTGRASS, 45),
                weighted(MoaRaceRegistry.TANGERINE, 45),
                weighted(MoaRaceRegistry.REDHOOD, 5),
                weighted(MoaRaceRegistry.MOONSTRUCK, 5));

        table(context, ParadiseLostBiomes.WISTERIA_WOODS_KEY,
                weighted(MoaRaceRegistry.GOLDENROD, 45),
                weighted(MoaRaceRegistry.STRAWBERRY_WISTAR, 45),
                weighted(MoaRaceRegistry.BLACKCURRANT_WISTAR, 29),
                weighted(MoaRaceRegistry.SCARLET, 2));

        table(context, ParadiseLostBiomes.AUTUMNAL_TUNDRA_KEY,
                weighted(MoaRaceRegistry.GREYHOUND, 15),
                weighted(MoaRaceRegistry.FROSTGRASS, 10));

        table(context, ParadiseLostBiomes.CONTINENTAL_PLATEAU_KEY,
                weighted(MoaRaceRegistry.GREYHOUND, 15),
                weighted(MoaRaceRegistry.FROSTGRASS, 10));

        table(context, ParadiseLostBiomes.CALCITE_CRAGLANDS_KEY,
                weighted(MoaRaceRegistry.GREYHOUND, 15),
                weighted(MoaRaceRegistry.FROSTGRASS, 10));

        table(context, ParadiseLostBiomes.HIGHLANDS_GRAND_GLADE_KEY,
                weighted(MoaRaceRegistry.HIGHLANDS_BLUE, 50),
                weighted(MoaRaceRegistry.HIGHLANDS_CYAN, 30),
                weighted(MoaRaceRegistry.MINTGRASS, 35));

        table(context, ParadiseLostBiomes.TRADEWINDS_KEY,
                weighted(MoaRaceRegistry.HIGHLANDS_BLUE, 45),
                weighted(MoaRaceRegistry.GOLDENROD, 20),
                weighted(MoaRaceRegistry.MINTGRASS, 30));

        context.register(ResourceKey.create(REGISTRY.key(), DEFAULT_ID), races(
                weighted(MoaRaceRegistry.FALLBACK, 1)));
    }

    private static void table(BootstrapContext<SpawnEntry> context, ResourceKey<Biome> biome, WeightedRace... entries) {
        context.register(ResourceKey.create(REGISTRY.key(), biome.location()), races(entries));
    }

    private static SpawnEntry races(WeightedRace... entries) {
        return new SpawnEntry(Optional.empty(), List.of(entries));
    }

    private static WeightedRace weighted(ResourceKey<MoaRace> race, int weight) {
        return new WeightedRace(race.location(), weight);
    }

    public static MoaRace getMoaFromSpawning(Level world, ResourceKey<Biome> biome, RandomSource random) {
        ResourceLocation id = getMoaRaceIdFromSpawning(world, biome, random);
        return MoaRaceRegistry.getOrFallback(world.registryAccess(), id);
    }

    public static ResourceLocation getMoaRaceIdFromSpawning(Level world, ResourceKey<Biome> biome, RandomSource random) {
        Registry<SpawnEntry> tables = REGISTRY.get(world.registryAccess());
        Holder<Biome> holder = world.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(biome);

        List<WeightedRace> pool = new ArrayList<>();
        tables.getOptional(biome.location()).ifPresent(table -> pool.addAll(table.races()));
        for (var entry : tables.entrySet()) {
            ResourceLocation id = entry.getKey().location();
            if (id.equals(biome.location()) || id.equals(DEFAULT_ID)) {
                continue;
            }
            entry.getValue().biomes().ifPresent(set -> {
                if (set.contains(holder)) {
                    pool.addAll(entry.getValue().races());
                }
            });
        }
        if (pool.isEmpty()) {
            tables.getOptional(DEFAULT_ID).ifPresent(table -> pool.addAll(table.races()));
        }
        ResourceLocation raceId = pick(world, pool, random);
        return raceId != null ? raceId : MoaRaceRegistry.FALLBACK_ID;
    }

    private static ResourceLocation pick(Level world, List<WeightedRace> candidates, RandomSource random) {
        List<WeightedEntry.Wrapper<ResourceLocation>> weighted = new ArrayList<>(candidates.size());
        for (WeightedRace entry : candidates) {
            if (resolve(world, entry.race()) != null) {
                weighted.add(WeightedEntry.wrap(entry.race(), entry.weight()));
            }
        }
        return WeightedRandom.getRandomItem(random, weighted).map(WeightedEntry.Wrapper::data).orElse(null);
    }

    private static MoaRace resolve(Level world, ResourceLocation raceId) {
        var race = MoaRaceRegistry.find(world.registryAccess(), raceId);
        if (race.isEmpty()) {
            if (WARNED_MISSING_RACES.add(raceId)) {
                ParadiseLost.LOG.error("moa_spawn refers to {} which is not a registered moa race; ignoring it", raceId);
            }
            return null;
        }
        return race.get();
    }

    public record SpawnEntry(Optional<HolderSet<Biome>> biomes, List<WeightedRace> races) {
        public static final Codec<SpawnEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                RegistryCodecs.homogeneousList(Registries.BIOME).optionalFieldOf("biomes").forGetter(SpawnEntry::biomes),
                WeightedRace.CODEC.listOf().fieldOf("races").forGetter(SpawnEntry::races)
        ).apply(instance, SpawnEntry::new));
    }

    public record WeightedRace(ResourceLocation race, int weight) {
        public static final Codec<WeightedRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("race").forGetter(WeightedRace::race),
                Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").forGetter(WeightedRace::weight)
        ).apply(instance, WeightedRace::new));
    }
}
