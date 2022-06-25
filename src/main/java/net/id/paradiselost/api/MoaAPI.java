package net.id.paradiselost.api;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.id.paradiselost.ParadiseLost;
import net.id.paradiselost.component.MoaGenes;
import net.id.paradiselost.entities.passive.moa.MoaAttributes;
import net.id.paradiselost.world.dimension.ParadiseLostBiomes;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;

/**
 * An API intended to aid creation of {@code MoaRace}s, and register
 * spawn probabilities based on biome and mating. It also has several
 * other useful tools regarding Moas.
 * <br>
 * ~ Jack
 * @author AzazelTheDemonLord
 * @see net.id.paradiselost.entities.passive.moa.MoaRaces
 */
public class MoaAPI {
    /**
     * If a {@code MoaRace} cannot be found by some method, it is recommended to use this in its place,
     * rather than returning null.
     */
    public static final MoaRace FALLBACK_MOA = new MoaRace(MoaAttributes.GROUND_SPEED, SpawnStatWeighting.TANK);
    
    /**
     * A map of all registered {@link MoaRace}s.
     */
    private static final Map<Identifier, MoaRace> MOA_RACE_REGISTRY = new Object2ObjectOpenHashMap<>();
    
    /**
     * The registry for storing {@code MoaRace} biome spawning information.
     * @see SpawnBucket
     */
    private static final Map<RegistryKey<Biome>, SpawnBucket> MOA_SPAWN_REGISTRY = new Object2ObjectOpenHashMap<>();
    
    /**
     * The "registry" for storing {@code MoaRace} breeding information.
     * It's really just a set of all {@code MatingEntries}.
     * @see MoaAPI.MatingEntry
     */
    private static final Set<MatingEntry> MOA_BREEDING_REGISTRY = new ObjectOpenHashSet<>();
    
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
     * Registers a new mating entry based on the input parameters.
     * @param child The {@code MoaRace} to be created
     * @param chance The probability that the child is created by breeding.
     * @param parentA The {@code MoaRace} bred with {@code parentB}
     * @param parentB The {@code MoaRace} bred with {@code parentA}
     */
    public static void registerBreeding(MoaRace child, float chance, MoaRace parentA, MoaRace parentB) {
        registerBreeding(child, createChanceCheck(chance), parentA, parentB);
    }
    
    /**
     * Registers a new mating entry based on the input parameters.
     * This automatically adds a check for these given parents.
     * @param child The {@code MoaRace} to be created
     * @param predicate Returns {@code true} if the child can be created by breeding.
     * @param parentA The {@code MoaRace} bred with {@code parentB}
     * @param parentB The {@code MoaRace} bred with {@code parentA}
     */
    public static void registerBreeding(MoaRace child, Predicate<MoaBreedingContext> predicate, MoaRace parentA, MoaRace parentB) {
        registerBreeding(child, predicate.and(createIdentityCheck(parentA, parentB)));
    }
    
    /**
     * Registers a new mating entry based on the input parameters.
     * This does not automatically complete a parent check.
     * @param child The {@code MoaRace} to be created
     * @param predicate Returns {@code true} if the child can be created by breeding.
     */
    public static void registerBreeding(MoaRace child, Predicate<MoaBreedingContext> predicate) {
        registerBreeding(new MatingEntry(child, predicate));
    }
    
    private static void registerBreeding(MatingEntry entry) {
        MOA_BREEDING_REGISTRY.add(entry);
    }
    
    /**
     * @param spawnBiome The biome to register the spawn weighting in.
     * @param race The {@code MoaRace} to register the spawn weighting of in the {@code spawnBiome}.
     * @param weight The spawn weight.
     */
    public static void registerSpawning(MoaRace race, int weight, RegistryKey<Biome> spawnBiome) {
        MOA_SPAWN_REGISTRY.computeIfAbsent(spawnBiome, key -> new SpawnBucket()).put(race, weight);
    }
    
    /**
     * @return The unique {@code MoaRace} corresponding to the given {@code Identifier}
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
     * @return The SpawnBucket of the given biome.
     * @see SpawnBucket
     */
    private static Optional<SpawnBucket> getSpawnBucket(RegistryKey<Biome> biome) {
        return Optional.ofNullable(MOA_SPAWN_REGISTRY.get(biome));
    }
    
    /**
     * @param biome The biome to get a random {@code MoaRace} from
     * @return A {@code MoaRace} registered to spawn in the given biome,
     * distributed proportionally based on percent chance of spawning.
     */
    @NotNull
    public static MoaRace getMoaFromSpawning(RegistryKey<Biome> biome, Random random) {
        return getSpawnBucket(biome)
            .map(bucket -> bucket.get(random))
            .orElse(MOA_SPAWN_REGISTRY.get(ParadiseLostBiomes.HIGHLANDS_PLAINS_KEY).get(random));
    }
    
    /**
     * @return A random {@code MoaRace} that can be bred from the provided context.
     * If none can be generated, the returned race is a random selection of the parents' races.
     * <br>Note: This may not return a fallback moa unless both parents' races are
     * {@link MoaAPI#FALLBACK_MOA}, or some breeding for the {@code FALLBACK_MOA} is registered, which
     * is not the case by default.
     */
    public static MoaRace getMoaFromBreeding(MoaBreedingContext ctx) {
        Optional<MatingEntry> childRace = MOA_BREEDING_REGISTRY.stream()
            .filter(matingEntry -> matingEntry.breedingRequirements.test(ctx))
            .findAny();
        return childRace.map(MatingEntry::race).orElseGet(() -> {
            // Don't return a fallback moa if it can be avoided
            if (ctx.parentA.getRace() == FALLBACK_MOA) {
                return ctx.parentB.getRace();
            }
            if (ctx.parentB.getRace() == FALLBACK_MOA) {
                return ctx.parentA.getRace();
            }
            // Select randomly from the parents' races.
            return ctx.world().getRandom().nextBoolean() ? ctx.parentA.getRace() : ctx.parentB.getRace();
        });
    }
    
    /**
     * @return A random {@code MoaRace} that can be bred from the provided context.
     * If none can be generated, the returned race is a random selection of the parents' races.
     * <br>Note: This may not return a fallback moa unless both parents' races are
     * {@link MoaAPI#FALLBACK_MOA}, or some breeding for the {@code FALLBACK_MOA} is registered, which
     * is not the case by default.
     */
    public static MoaRace getMoaFromBreeding(MoaGenes parentA, MoaGenes parentB, World world, BlockPos pos) {
        MoaBreedingContext context = new MoaBreedingContext(parentA, parentB, world, pos);
        return getMoaFromBreeding(context);
    }
    
    private static Predicate<MoaBreedingContext> createIdentityCheck(MoaRace raceA, MoaRace raceB) {
        return (ctx) -> (raceA == ctx.parentA.getRace() && raceB == ctx.parentB.getRace())
                        || (raceB == ctx.parentA.getRace() && raceA == ctx.parentB.getRace());
    }
    
    private static Predicate<MoaBreedingContext> createChanceCheck(float chance) {
        return (ctx) -> ctx.world.getRandom().nextFloat() < chance;
    }
    
    public static record MoaBreedingContext(MoaGenes parentA, MoaGenes parentB, World world, BlockPos pos){
    }
    
    // Ideally this shouldn't be an enum
    // This can also probably be hidden away in entities/passive/moa/.
    /**
     * Pretty much "classes" for MoaRaces. Such that a Moa that could
     * be considered a "tank" would be fairly slow, for example.
     * The classes affect speed, gliding speed, jump strength, and max health.
     */
    public enum SpawnStatWeighting {
        // Isn't this array of numbers nice? Could probably be defined in json instead.
        SPEED(0.08F, 0.1F, 0.02F, 0.03F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.04F, -0.08F, 0F, -0.01F, 0, 6),
        ENDURANCE(0.023F, 0.06F, 0.02F, 0.02F, -0.085F, -0.08F, -0.01F, -0.02F, 2, 8),
        TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.02F, -0.01F, 6, 6),
        MYTHICAL_SPEED(0.31F, 0.17F, 0.082F, 0.0375F, 0F, -0.1F, 0F, -0.01F, 0, 8),
        MYTHICAL_GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.085F, -0.085F, 0F, -0.01F, 0, 6),
        MYTHICAL_TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, -0.05F, -0.03F, -0.01F, 14, 6),
        MYTHICAL_ALL(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, -0.085F, -0.03F, -0.01F, 14, 6)
        ;
        
        private final ImmutableMap<MoaAttributes, Weighting> data;
        
        SpawnStatWeighting(float baseGroundSpeed, float groundSpeedVariance, float baseGlidingSpeed, float glidingSpeedVariance, float baseGlidingDecay, float glidingDecayVariance, float baseJumpStrength, float jumpStrengthVariance, float baseMaxHealth, float maxHealthVariance) {
            var builder = ImmutableMap.<MoaAttributes, Weighting>builder();
            builder.put(MoaAttributes.GROUND_SPEED, new Weighting(baseGroundSpeed, groundSpeedVariance));
            builder.put(MoaAttributes.GLIDING_SPEED, new Weighting(baseGlidingSpeed, glidingSpeedVariance));
            builder.put(MoaAttributes.GLIDING_DECAY, new Weighting(baseGlidingDecay, glidingDecayVariance));
            builder.put(MoaAttributes.JUMPING_STRENGTH, new Weighting(baseJumpStrength, jumpStrengthVariance));
            builder.put(MoaAttributes.MAX_HEALTH, new Weighting(baseMaxHealth, maxHealthVariance));
            builder.put(MoaAttributes.DROP_MULTIPLIER, new Weighting(0, 0));
            data = builder.build();
        }
        
        /**
         * @return A float representing the stat value for the given attribute
         */
        @SuppressWarnings("ConstantConditions")
        public float configure(MoaAttributes attribute, MoaRace race, Random random) {
            Weighting statData = data.get(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base + (random.nextFloat() * statData.variance) * (race.defaultAffinity == attribute ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F) : 1F)));
        }
        
        private static record Weighting(float base, float variance){
        }
    }
    
    /**
     * A container for all stats pertaining to a certain {@code MoaRace}
     * @param defaultAffinity The default "affinity" of the MoaRace. What it improves on most generation-over-generation.
     * @param statWeighting How the MoaRace is predisposed to be good certain things. See {@link SpawnStatWeighting}.
     * @param glowing Whether the created {@code MoaRace} will glow.
     * @param legendary Whether the created {@code MoaRace} will be legendary
     * @param particles The particles emitted by this MoaRace, if it is legendary
     */
    public static record MoaRace(MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting,
                                 boolean glowing, boolean legendary, ParticleType<?> particles) {
        public MoaRace(MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting){
            this(defaultAffinity, statWeighting, false, false, ParticleTypes.ENCHANT);
        }
        
        /**
         * @return The ID of this {@code MoaRace} as provided by the {@code MOA_RACE_REGISTRY}.
         */
        public Identifier getId(){
            for (var entry : MOA_RACE_REGISTRY.entrySet()){
                if (entry.getValue() == this){
                    return entry.getKey();
                }
            }
            ParadiseLost.LOG.error("MoaAPI.MoaRace.getId() called before race was registered. Report this to somebody.");
            return FALLBACK_MOA.getId();
        }
        
        /**
         * @return The translation key associated with this MoaRace.
         */
        public String getTranslationKey() {
            Identifier id = this.getId();
            return "moa.race." + id.getNamespace() + "." + id.getPath();
        }
    }
    
    /**
     * A collection of MoaRaces and their weights for a specific biome.
     */
    private static class SpawnBucket {
        private final Map<MoaRace, Integer> weights = new HashMap<>();
        private MoaRace heaviest = FALLBACK_MOA;
        private int totalWeight = 0;
        
        /**
         * Adds a MoaRace and Spawn weighting pair to this SpawnBucket.
         * A higher weight is more likely to be chosen.
         */
        private void put(MoaRace race, int weight) {
            if (weight < 1) {
                throw new IllegalArgumentException(race.getId().toString() + " has an invalid weight, must be 1 or higher!");
            }
            
            if (weight > weights.getOrDefault(heaviest, 0)) {
                heaviest = race;
            }
            
            weights.put(race, weight);
            totalWeight += weight;
        }
        
        /**
         * @return A random MoaRace. Races with higher weights in this bucket are more likely.
         * If no races have been added to this bucket, returns a {@link MoaAPI#FALLBACK_MOA}
         */
        private MoaRace get(Random random) {
            // Looks for the first entry thats weight is greater than a random number
            var entryOptional = weights.entrySet().stream()
                .filter(entry -> random.nextInt(totalWeight) < entry.getValue())
                .findAny();
            return entryOptional.map(Map.Entry::getKey).orElse(heaviest);
        }
        
    }
    
    /**
     * This records how a MoaRace can result from breeding. There isn't a limit
     * on the number of MatingEntries per MoaRace.
     */
    private static record MatingEntry(MoaRace race, Predicate<MoaBreedingContext> breedingRequirements) {
    }
    
    static {
        register(ParadiseLost.locate("fallback"), FALLBACK_MOA);
    }
}
