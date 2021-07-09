package com.aether.api;

import com.aether.Aether;
import com.aether.world.dimension.AetherDimension;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Identifier;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;

import java.util.*;

public class MoaAPI {

    public static final Identifier FALLBACK_ID = Aether.locate("fallback");
    public static final Race FALLBACK_MOA = new Race(FALLBACK_ID, Aether.locate("textures/entity/moas/highlands/blue.png"), MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED);

    private static final Object2ObjectOpenHashMap<Identifier, Race> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<RegistryKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();

    public static void init() {
        //  Highlands
        final Race highlandsBlue = register("highlands_blue", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED, 50, "textures/entity/moas/highlands/blue.png");
        final Race goldenrod = register("goldenrod", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.JUMPING_STRENGTH, SpawnStatWeighting.ENDURANCE, 10, "textures/entity/moas/highlands/goldenrod.png");
        final Race mintgrass = register("mintgrass", AetherDimension.HIGHLANDS_PLAINS, MoaAttributes.GLIDING_SPEED, SpawnStatWeighting.SPEED, 40, "textures/entity/moas/highlands/mintgrass.png");

        final Race tangerine = register("tangerine", AetherDimension.HIGHLANDS_FOREST, MoaAttributes.JUMPING_STRENGTH, SpawnStatWeighting.SPEED, 15, "textures/entity/moas/highlands/tangerine.png");
        append(AetherDimension.HIGHLANDS_FOREST, highlandsBlue.id, 50);
        append(AetherDimension.HIGHLANDS_FOREST, goldenrod.id, 35);

        final Race foxtrot = register("foxtrot", AetherDimension.HIGHLANDS_THICKET, MoaAttributes.DROP_MULTIPLIER, SpawnStatWeighting.TANK, 5, "textures/entity/moas/highlands/foxtrot.png");
        append(AetherDimension.HIGHLANDS_THICKET, tangerine.id, 50);
        append(AetherDimension.HIGHLANDS_THICKET, mintgrass.id, 45);

        final Race strawberryWistar = register("strawberry_wistar", AetherDimension.WISTERIA_WOODS, MoaAttributes.GLIDING_DECAY, SpawnStatWeighting.SPEED, 49, "textures/entity/moas/highlands/strawberry_wistar.png");
        final Race scarlet = register("scarlet", AetherDimension.WISTERIA_WOODS, MoaAttributes.GLIDING_SPEED, SpawnStatWeighting.ENDURANCE, 2, "textures/entity/moas/highlands/scarlet.png");
        append(AetherDimension.WISTERIA_WOODS, goldenrod.id, 49);
    }

    public static Race register(String name, RegistryKey<Biome> spawnBiome, MoaAttributes affinity, SpawnStatWeighting spawnStats, int weight, String texturePath) {
        Identifier raceId = Aether.locate(name);
        Identifier texture = Aether.locate(texturePath);

        final Race race = new Race(raceId, texture, affinity, spawnStats);
        MOA_RACE_REGISTRY.put(raceId, race);
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(raceId, weight);
        return race;
    }

    public static void append(RegistryKey<Biome> spawnBiome, Identifier raceId, int weight) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(raceId, weight);
    }

    public static Race getRace(Identifier raceId) {
        return MOA_RACE_REGISTRY.getOrDefault(raceId, FALLBACK_MOA);
    }

    public static Optional<SpawnBucket> getSpawnBucket(RegistryKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }

    public static Race getMoaForBiome(RegistryKey<Biome> biome, Random random) {
        Optional<Identifier> raceOptional = Optional.ofNullable(getSpawnBucket(biome).map(bucket -> bucket.get(random)).orElse(MOA_SPAWN_REGISTRY.get(AetherDimension.HIGHLANDS_PLAINS).get(random)));
        return raceOptional.map(MoaAPI::getRace).orElse(FALLBACK_MOA);
    }

    public static record Race(Identifier id, Identifier texturePath, MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting) {}

    private static record SpawnBucketEntry(Identifier id, int weight) {
        public boolean test(Random random, int whole) {
            return random.nextInt(whole) < weight;
        }
    }

    private static class SpawnBucket {

        private final List<SpawnBucketEntry> entries = new ArrayList<>();
        private SpawnBucketEntry heaviest = new SpawnBucketEntry(FALLBACK_ID, 0);
        private int totalWeight;

        public void put(Identifier raceId, int weight) {

            if(weight < 1) {
                throw new IllegalArgumentException(raceId.toString() + " has an invalid weight, must be 1 or higher!");
            }

            SpawnBucketEntry entry = new SpawnBucketEntry(raceId, weight);

            if(weight > heaviest.weight) {
                heaviest = entry;
            }

            entries.add(entry);
            totalWeight += weight;
        }

        public Identifier get(Random random) {
            if(entries.size() == 1) {
                return entries.get(0).id;
            }
            Collections.shuffle(entries);
            Optional<SpawnBucketEntry> entryOptional = entries.stream().filter(entry -> entry.test(random, totalWeight)).findFirst();
            return entryOptional.map(SpawnBucketEntry::id).orElse(heaviest.id);
        }

    }

    private static record SpawnStatData(float base, float variance) {}

    public enum SpawnStatWeighting {
        SPEED(0.08F, 0.1F, 0.02F, 0.03F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.04F, -0.08F, 0F, -0.01F, 0, 6),
        ENDURANCE(0.023F, 0.06F, 0.02F, 0.02F, -0.085F, -0.08F, -0.01F, -0.02F, 2, 8),
        TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.02F, -0.01F, 6, 6),
        MYTHICAL_SPEED(0.31F, 0.17F, 0.082F, 0.0375F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        MYTHICAL_GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.085F, -0.085F, 0F, -0.01F, 0, 6),
        MYTHICAL_TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.03F, -0.01F, 14, 6),
        MYTHICAL_ALL(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, -0.085F, -0.03F, -0.01F, 14, 6),;

        public final ImmutableMap<MoaAttributes, SpawnStatData> data;

        SpawnStatWeighting(float baseGroundSpeed, float groundSpeedVariance, float baseGlidingSpeed, float glidingSpeedVariance, float baseGlidingDecay, float glidingDecayVariance, float baseJumpStrength, float jumpStrengthVariance, float baseMaxHealth, float maxHealthVariance) {
            var builder = ImmutableMap.<MoaAttributes, SpawnStatData>builder();
            builder.put(MoaAttributes.GROUND_SPEED, new SpawnStatData(baseGroundSpeed, groundSpeedVariance));
            builder.put(MoaAttributes.GLIDING_SPEED, new SpawnStatData(baseGlidingSpeed, glidingSpeedVariance));
            builder.put(MoaAttributes.GLIDING_DECAY, new SpawnStatData(baseGlidingDecay, glidingDecayVariance));
            builder.put(MoaAttributes.JUMPING_STRENGTH, new SpawnStatData(baseJumpStrength, jumpStrengthVariance));
            builder.put(MoaAttributes.MAX_HEALTH, new SpawnStatData(baseMaxHealth, maxHealthVariance));
            builder.put(MoaAttributes.DROP_MULTIPLIER, new SpawnStatData(0, 0));
            data = builder.build();
        }

        public float configure(MoaAttributes attribute, Race race, Random random) {
            SpawnStatData statData = data.get(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base + (random.nextFloat() * statData.variance) * (race.defaultAffinity == attribute ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F) : 1F)));
        }
    }
}
