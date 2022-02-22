package net.id.aether.api;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Function4;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.id.aether.Aether;
import net.id.aether.component.MoaGenes;
import net.id.aether.entities.passive.moa.MoaAttributes;
import net.id.aether.world.dimension.AetherBiomes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.*;
import java.util.function.BiPredicate;

/**
 * An API intended to aid creation of {@code MoaRace}s, and register
 * spawn probabilities based on biome and mating. It also has several
 * other useful tools regarding Moas.
 * <br><br>
 * ~ Jack
 * @author AzazelTheDemonLord
 */

public class MoaAPI {

    /**
     * If a {@code MoaRace} cannot be found by some method, it is recommended to use this in its place.
     */
    public static final MoaRace FALLBACK_MOA = new MoaRace(MoaAttributes.GROUND_SPEED, SpawnStatWeighting.SPEED);

    /**
     * Rather than a {@code Registry} registry, this uses a
     * {@link Object2ObjectOpenHashMap} registry, as a full
     * {@code Registry} is not necessary for our purposes.
     * @see MoaRace
     */
    private static final Object2ObjectOpenHashMap<Identifier, MoaRace> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();

    /**
     * The registry for storing {@code MoaRace} biome spawning information.
     * @see SpawnBucket
     */
    private static final Object2ObjectOpenHashMap<RegistryKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();

    /**
     * The "registry" for storing {@code MoaRace} breeding information.
     * It's really just a list of all {@code MatingEntries}.
     * @see MoaAPI.MatingEntry
     */
    private static final List<MatingEntry> MOA_BREEDING_REGISTRY = new ArrayList<>();

    /**
     * @param name The unique {@code Identifier} identifying this particular {@code MoaRace}
     * @param race The {@code MoaRace} to register
     * @return The registered {@code MoaRace}. This is always equal to {@code race}.
     * @author Jack Papel
     */
    public static MoaRace register(Identifier name, MoaRace race){
        MOA_RACE_REGISTRY.put(name, race);
        return race;
    }

    /**
     * Registers a new {@link MoaAPI.MatingEntry} based on the input parameters.
     * @param child The {@code MoaRace} to be created by the breeding of {@code parentA} and
     *              {@code parentB}, with a {@code chance} probability
     * @param parentA The {@code MoaRace} that when bred with {@code parentB}, has a {@code chance}
     *                probability of creating a {@code child} {@code MoaRace}
     * @param parentB The {@code MoaRace} that when bred with {@code parentA}, has a {@code chance}
     *                probability of creating a {@code child} {@code MoaRace}
     * @param chance The percent probability that this race is created by breeding.
     */
    public static void registerBreedingChance(MoaRace child, MoaRace parentA, MoaRace parentB, float chance) {
        registerBreedingPredicate(child, parentA, parentB, createChanceCheck(chance));
    }

    /**
     * Registers a new {@link MoaAPI.MatingEntry} based on the input parameters.
     * @param child The {@code MoaRace} to be created by the breeding of {@code parentA} and
     *              {@code parentB} if the {@code breedingPredicate} is met.
     * @param parentA The {@code MoaRace} that when bred with {@code parentB}, creates a
     *                {@code child} {@code MoaRace} if the {@code breedingPredicate} is met.
     * @param parentB The {@code MoaRace} that when bred with {@code parentA}, creates a
     *                {@code child} {@code MoaRace} if the {@code breedingPredicate} is met.
     * @param breedingPredicate The predicate that determines whether {@code child} is created by
     *                          the breeding of a moa of race {@code parentA} and of race
     *                          {@code parentB}.
     */
    public static void registerBreedingPredicate(MoaRace child, MoaRace parentA, MoaRace parentB, Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> breedingPredicate) {
        registerBreeding(new MatingEntry(child, createIdentityCheck(parentA, parentB), breedingPredicate));
    }

    /**
     * @param spawnBiome The biome to register the spawn weighting in.
     * @param child The {@code MoaRace} to register the spawn weighting of in the {@code spawnBiome} biome.
     * @param weight The spawn weight.
     */
    public static void registerBiomeSpawnWeighting(RegistryKey<Biome> spawnBiome, MoaRace child, int weight) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(child, weight);
    }

    /**
     * Registers {@code MatingEntry} {@code entry} into the {@code MOA_BREEDING_REGISTRY}.
     * @param entry The {@link MoaAPI.MatingEntry} to register
     */
    private static void registerBreeding(MatingEntry entry) {
        MOA_BREEDING_REGISTRY.add(entry);
    }

    /**
     * @param raceId The {@code Identifier} of the {@code MoaRace}
     * @return The unique {@code MoaRace} of a given {@code Identifier}
     */
    public static MoaRace getRace(Identifier raceId) {
        return MOA_RACE_REGISTRY.getOrDefault(raceId, FALLBACK_MOA);
    }

    /**
     * @return All registered {@code MoaRaces} as an {@code Iterator}
     */
    public static Iterator<MoaRace> getRegisteredRaces() {
        return MOA_RACE_REGISTRY.values().iterator();
    }

    /**
     * @param biome The biome to get the SpawnBucket of
     * @return The SpawnBucket of the given biome.
     * @see SpawnBucket
     */
    public static Optional<SpawnBucket> getSpawnBucket(RegistryKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }

    /**
     * @param biome The biome to get a random {@code MoaRace} from
     * @return A {@code MoaRace} registered to spawn in the given biome,
     * distributed proportionally based on percent chance of spawning.
     */
    public static MoaRace getMoaForBiome(RegistryKey<Biome> biome, Random random) {
        Optional<MoaRace> raceOptional =
                Optional.ofNullable(getSpawnBucket(biome)
                        .map(bucket -> bucket.get(random))
                        .orElse(MOA_SPAWN_REGISTRY.get(AetherBiomes.HIGHLANDS_PLAINS_KEY).get(random)));
        return raceOptional.orElse(FALLBACK_MOA);
    }

    /**
     * Returns a random {@code MoaRace} generated by the breeding of {@code parentA} and {@code parentB}
     */
    public static MoaRace getMoaForBreeding(MoaGenes parentA, MoaGenes parentB, World world, BlockPos pos) {
        var childRace =
                MOA_BREEDING_REGISTRY.stream()
                        .filter(matingEntry -> matingEntry.identityCheck.test(parentA.getRace(), parentB.getRace()) && matingEntry.additionalChecks.apply(parentA, parentB, world, pos))
                        .findAny();
        return childRace.map(MatingEntry::get).orElse(world.getRandom().nextBoolean() ? parentA.getRace() : parentB.getRace());
    }

    /**
     * @return A {@code BiPredicate} which tests whether raceA and raceB are
     * equal to a given parentA and parentB, respectively.
     */
    public static BiPredicate<MoaRace, MoaRace> createIdentityCheck(MoaRace raceA, MoaRace raceB) {
        return (parentA, parentB) -> (raceA == parentA && raceB == parentB) || (raceB == parentA && raceA == parentB);
    }

    /**
     * @return A {@code Function4} which tests whether a random {@code float}
     * is less than a given {@code float} {@code chance}.
     */
    public static Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> createChanceCheck(float chance) {
        return (parentA, parentB, world, pos) -> world.getRandom().nextFloat() < chance;
    }

    /**
     * An enum of various default weightings of spawn stats. Such that a Moa that could
     * be considered a "tank" would be fairly slow, for example.
     */
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

    /**
     * A container for all stats pertaining to a certain {@code MoaRace}
     * @param defaultAffinity The default "affinity" of the MoaRace. What it's good at.
     *                        <a, href="https://discord.gg/eRsJ6F3Wng">Ask Azzy</a> how this is different to {@link SpawnStatWeighting}.
     * @param statWeighting How the well MoaEntity is predisposed at certain things. See {@link SpawnStatWeighting}.
     * @param glowing Whether the created {@code MoaRace} will glow.
     * @param legendary Whether the created {@code MoaRace} will be legendary
     * @param particles The particles emitted by this MoaRace, if it is legendary
     */
    public static record MoaRace(MoaAttributes defaultAffinity,
                                 SpawnStatWeighting statWeighting, boolean glowing, boolean legendary,
                                 ParticleType<?> particles) {

        public MoaRace(MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting){
            this(defaultAffinity, statWeighting, false, false, ParticleTypes.ENCHANT);
        }

        /**
         * @return The ID of this {@code MoaRace} as provided by {@code MOA_RACE_REGISTRY}.
         */
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

        public String getTranslationKey() {
            Identifier id = this.getId();
            return "moa.race." + id.getNamespace() + "." + id.getPath();
        }
    }

    /**
     * A record of a MoaRace's weight in a specific SpawnBucket.
     * Weight in a random number sense, that is - not a mass sense.
     */
    private static record SpawnBucketEntry(MoaRace race, int weight) {
        public boolean test(Random random, int whole) {
            return random.nextInt(whole) < weight;
        }
    }

    /**
     * A collection of MoaRaces and their weights in the form of a {@link SpawnBucketEntry} for a specific biome.
     */
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

    /**
     * This records how a MoaRace can result from breeding. There isn't a limit
     * on the number of MatingEntries per MoaRace.
     */
    private static record MatingEntry(MoaRace race, BiPredicate<MoaRace, MoaRace> identityCheck,
                                      Function4<MoaGenes, MoaGenes, World, BlockPos, Boolean> additionalChecks) {
        public MoaRace get() {
            return race;
        }
    }

    /**
     * <a, href="https://discord.gg/eRsJ6F3Wng">Ask Azzy</a> about this. It's a lower bound and a variance for spawn stat data, it seems.
     */
    private static record SpawnStatData(float base, float variance) {
    }

    static {
        register(Aether.locate("fallback"), FALLBACK_MOA);
    }
}
