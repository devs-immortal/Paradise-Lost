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

public class MoaAPI {
    public static final MoaRace FALLBACK_MOA = new MoaRace(Aether.locate("textures/entity/moa/highlands_blue.png"), MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED, false, false, ParticleTypes.ENCHANT);

    private static final Object2ObjectOpenHashMap<Identifier, MoaRace> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final Object2ObjectOpenHashMap<RegistryKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();
    private static final List<MatingEntry> MOA_BREEDING_REGISTRY = new ArrayList<>();

    public static MoaRace register(Identifier name, MoaAttributes affinity, SpawnStatWeighting spawnStats, boolean glowing, boolean legendary, ParticleType<?> particles, Identifier texturePath){
        final MoaRace race = new MoaRace(texturePath, affinity, spawnStats, glowing, legendary, particles);
        MOA_RACE_REGISTRY.put(name, race);

        return race;
    }

    public static void registerBreedingChance(MoaRace child, MoaRace parentA, MoaRace parentB, float chance) {
        registerBreedingPredicate(child, parentA, parentB, createChanceCheck(chance));
    }

    public static void registerBreedingPredicate(MoaRace child, MoaRace parentA, MoaRace parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> breedingPredicate) {
        registerBreeding(new MatingEntry(child, createIdentityCheck(parentA, parentB), breedingPredicate));
    }

    public static void registerBiomeSpawnWeighting(RegistryKey<Biome> spawnBiome, MoaRace child, int weight) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(child, weight);
    }

    private static void registerBreeding(MatingEntry entry) {
        MOA_BREEDING_REGISTRY.add(entry);
    }

    public static MoaRace getRace(Identifier raceId) {
        return MOA_RACE_REGISTRY.getOrDefault(raceId, FALLBACK_MOA);
    }

    public static Iterator<MoaRace> getRegisteredRaces() {
        return MOA_RACE_REGISTRY.values().iterator();
    }

    public static Optional<SpawnBucket> getSpawnBucket(RegistryKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }

    public static MoaRace getMoaForBiome(RegistryKey<Biome> biome, Random random) {
        Optional<MoaRace> raceOptional =
                Optional.ofNullable(getSpawnBucket(biome)
                        .map(bucket -> bucket.get(random))
                        .orElse(MOA_SPAWN_REGISTRY.get(AetherDimension.HIGHLANDS_PLAINS).get(random)));
        return raceOptional.orElse(FALLBACK_MOA);
    }

    public static MoaRace getMoaForBreeding(MoaGenes parentA, MoaGenes parentB, World world, BlockPos pos) {
        var childRace =
                MOA_BREEDING_REGISTRY.stream()
                        .filter(matingEntry -> matingEntry.identityCheck.test(parentA.getRace(), parentB.getRace()) && matingEntry.additionalChecks.apply(parentA, parentB, world, pos))
                        .findAny();
        return childRace.map(MatingEntry::get).orElse(world.getRandom().nextBoolean() ? parentA.getRace() : parentB.getRace());
    }

    @Environment(EnvType.CLIENT)
    public static String formatForTranslation(MoaRace race) {
        return "moa.race." + race.getId().getPath();
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

    public static record MoaRace(Identifier texturePath, MoaAttributes defaultAffinity,
                                 SpawnStatWeighting statWeighting, boolean glowing, boolean legendary,
                                 ParticleType<?> particles) {

        public Identifier getId(){
            if (this == FALLBACK_MOA) {
                return Aether.locate("fallback");
            }
            for (var entry : MOA_RACE_REGISTRY.entrySet()){
                if (entry.getValue() == this){
                    return entry.getKey();
                }
            }
            System.out.println("getId() called before race was registered. You had to mess up so bad to get this error.");
            return FALLBACK_MOA.getId();
        }
    }

    private static record SpawnBucketEntry(MoaRace race, int weight) {
        public boolean test(Random random, int whole) {
            return random.nextInt(whole) < weight;
        }
    }

    private static class SpawnBucket {

        private final List<SpawnBucketEntry> entries = new ArrayList<>();
        private SpawnBucketEntry heaviest = new SpawnBucketEntry(FALLBACK_MOA, 0);
        private int totalWeight;

        public void put(MoaRace race, int weight) {

            if (weight < 1) {
                throw new IllegalArgumentException(race.getId().toString() + " has an invalid weight, must be 1 or higher!");
            }

            SpawnBucketEntry entry = new SpawnBucketEntry(race, weight);

            if (weight > heaviest.weight) {
                heaviest = entry;
            }

            entries.add(entry);
            totalWeight += weight;
        }

        public MoaRace get(Random random) {
            if (entries.size() == 1) {
                return entries.get(0).race;
            }
            Collections.shuffle(entries);
            Optional<SpawnBucketEntry> entryOptional = entries.stream().filter(entry -> entry.test(random, totalWeight)).findFirst();
            return entryOptional.map(SpawnBucketEntry::race).orElse(heaviest.race);
        }

    }

    private static record MatingEntry(MoaRace race, BiPredicate<MoaRace, MoaRace> identityCheck,
                                      Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> additionalChecks) {
        public MoaRace get() {
            return race;
        }
    }

    private static record SpawnStatData(float base, float variance) {
    }
}
