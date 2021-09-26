package net.id.aether.api;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Function4;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.id.aether.component.MoaGenes;
import net.id.aether.entities.passive.moa.MoaAttributes;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.*;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public class MoaAPI {
    public static final Identifier FALLBACK_ID = Aether.locate("fallback");
    public static final MoaRace FALLBACK_MOA = new MoaRace(FALLBACK_ID, Aether.locate("textures/entity/moas/highlands/blue.png"), MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT);

    private static final Object2ObjectOpenHashMap<Identifier, MoaRace> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<RegistryKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final List<MatingEntry> MOA_BREEDING_REGISTRY = new ArrayList<>();

    public static MoaRace register(Identifier name, MoaAttributes affinity, SpawnStatWeighting spawnStats, boolean glowing, boolean legendary, ParticleType<?> particles, Identifier texturePath){
        final MoaRace race = new MoaRace(name, texturePath, affinity, spawnStats, glowing, legendary, particles);
        MOA_RACE_REGISTRY.put(name, race);

        return race;
    }

    public static void registerBreedingChance(Identifier raceId, MoaRace parentA, MoaRace parentB, float chance) {
        registerBreedingPredicate(raceId, parentA, parentB, createChanceCheck(chance));
    }

    public static void registerBreedingPredicate(Identifier raceId, MoaRace parentA, MoaRace parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> breedingPredicate) {
        registerBreeding(new MatingEntry(raceId, createIdentityCheck(parentA, parentB), breedingPredicate));
    }

    public static void registerBiomeSpawnWeighting(RegistryKey<Biome> spawnBiome, Identifier raceId, int weight) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(raceId, weight);
    }

    private static void registerBreeding(MatingEntry entry) {
        MOA_BREEDING_REGISTRY.add(entry);
    }

    public static MoaRace getRace(Identifier raceId) {
        return MOA_RACE_REGISTRY.getOrDefault(raceId, FALLBACK_MOA);
    }

    public static Iterator<Identifier> getRegisteredRaces() {
        return MOA_RACE_REGISTRY.keySet().iterator();
    }

    public static Optional<SpawnBucket> getSpawnBucket(RegistryKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }

    public static MoaRace getMoaForBiome(RegistryKey<Biome> biome, Random random) {
        Optional<Identifier> raceOptional =
                Optional.ofNullable(getSpawnBucket(biome)
                        .map(bucket -> bucket.get(random))
                        .orElse(MOA_SPAWN_REGISTRY.get(AetherDimension.HIGHLANDS_PLAINS).get(random)));
        return raceOptional.map(MoaAPI::getRace).orElse(FALLBACK_MOA);
    }

    public static MoaRace getMoaForBreeding(MoaGenes parentA, MoaGenes parentB, World world, BlockPos pos) {
        var childRace =
                MOA_BREEDING_REGISTRY.stream()
                        .filter(matingEntry -> matingEntry.identityCheck.test(parentA.getRace(), parentB.getRace()) && matingEntry.additionalChecks.apply(parentA, parentB, world, pos))
                        .findAny();
        return childRace.map(MatingEntry::get).orElse(world.getRandom().nextBoolean() ? parentA.getRace() : parentB.getRace());
    }

    @Environment(EnvType.CLIENT)
    public static String formatForTranslation(Identifier raceId) {
        return "moa.race." + raceId.getPath();
    }

    @Environment(EnvType.CLIENT)
    public static String formatForTranslation(MoaAttributes attribute) {
        return attribute != null ? "moa.attribute." + attribute.name().toLowerCase() : "???";
    }

    public static BiPredicate<MoaRace, MoaRace> createIdentityCheck(MoaRace raceA, MoaRace raceB) {
        return (parentA, parentB) -> (raceA == parentA && raceB == parentB) || (raceB == parentA && raceA == parentB);
    }

    public static Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> createChanceCheck(float chance) {
        return (parentA, parentB, world, pos) -> world.getRandom().nextFloat() < chance;
    }

    public enum SpawnStatWeighting {
        SPEED(0.08F, 0.1F, 0.02F, 0.03F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.04F, -0.08F, 0F, -0.01F, 0, 6),
        ENDURANCE(0.023F, 0.06F, 0.02F, 0.02F, -0.085F, -0.08F, -0.01F, -0.02F, 2, 8),
        TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.02F, -0.01F, 6, 6),
        MYTHICAL_SPEED(0.31F, 0.17F, 0.082F, 0.0375F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        MYTHICAL_GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.085F, -0.085F, 0F, -0.01F, 0, 6),
        MYTHICAL_TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.03F, -0.01F, 14, 6),
        MYTHICAL_ALL(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, -0.085F, -0.03F, -0.01F, 14, 6),
        ;

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

        public float configure(MoaAttributes attribute, MoaRace race, Random random) {
            SpawnStatData statData = data.get(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base + (random.nextFloat() * statData.variance) * (race.defaultAffinity == attribute ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F) : 1F)));
        }
    }

    public static record MoaRace(Identifier id, Identifier texturePath, MoaAttributes defaultAffinity,
                                 SpawnStatWeighting statWeighting, boolean glowing, boolean legendary,
                                 ParticleType<?> particles) {
    }

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

            if (weight < 1) {
                throw new IllegalArgumentException(raceId.toString() + " has an invalid weight, must be 1 or higher!");
            }

            SpawnBucketEntry entry = new SpawnBucketEntry(raceId, weight);

            if (weight > heaviest.weight) {
                heaviest = entry;
            }

            entries.add(entry);
            totalWeight += weight;
        }

        public Identifier get(Random random) {
            if (entries.size() == 1) {
                return entries.get(0).id;
            }
            Collections.shuffle(entries);
            Optional<SpawnBucketEntry> entryOptional = entries.stream().filter(entry -> entry.test(random, totalWeight)).findFirst();
            return entryOptional.map(SpawnBucketEntry::id).orElse(heaviest.id);
        }

    }

    private static record MatingEntry(Identifier race, BiPredicate<MoaRace, MoaRace> identityCheck,
                                      Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> additionalChecks) {
        public MoaRace get() {
            return getRace(race);
        }
    }

    private static record SpawnStatData(float base, float variance) {
    }
}
