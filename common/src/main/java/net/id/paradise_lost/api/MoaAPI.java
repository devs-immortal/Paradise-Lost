package net.id.paradise_lost.api;

import com.google.common.collect.ImmutableMap;
import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.ParadiseLost;
import net.id.paradise_lost.component.MoaGenes;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.registry.MoaBreedingRegistry;
import net.id.paradise_lost.registry.MoaRaceRegistry;
import net.id.paradise_lost.registry.MoaSpawnRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Optional;

public class MoaAPI {

    public static Optional<MoaRace> findRace(ResourceLocation raceId) {
        if (raceId == null) {
            return Optional.empty();
        }
        return MoaRaceRegistry.MOA_RACES.getOptional(raceId);
    }

    public static MoaRace getRace(ResourceLocation raceId) {
        return findRace(raceId).orElseGet(MoaRaceRegistry.FALLBACK_MOA);
    }

    public static MoaRace getFallbackRace() {
        return MoaRaceRegistry.FALLBACK_MOA.get();
    }

    public static Iterator<MoaRace> getRegisteredRaces() {
        return MoaRaceRegistry.MOA_RACES.iterator();
    }

    @NotNull
    public static MoaRace getMoaFromSpawning(Level world, ResourceKey<Biome> biome, RandomSource random) {
        return MoaSpawnRegistry.getMoaFromSpawning(world, biome, random);
    }

    public static MoaRace getMoaFromBreeding(MoaBreedingContext ctx) {
        return MoaBreedingRegistry.getMoaFromBreeding(ctx);
    }

    public static MoaRace getMoaFromBreeding(MoaGenes parentA, MoaGenes parentB, Level world, BlockPos pos) {
        return getMoaFromBreeding(new MoaBreedingContext(parentA, parentB, world, pos));
    }

    public record MoaBreedingContext(MoaGenes parentA, MoaGenes parentB, Level world, BlockPos pos) {
    }

    public enum SpawnStatWeighting {

        SPEED(0.08F, 0.1F, 0.02F, 0.03F, -0.02F, 0.2F, 0F, -0.01F, 0, 8, 0f, 0.02f),
        GLIDE(0.013F, 0.08F, 0.035F, 0.039F, -0.01F, 0.05F, 0F, 0.005F, 0, 6, 0f, 0.02f),
        ENDURANCE(0.023F, 0.06F, 0.02F, 0.02F, -0.02F, 0.04F, -0.01F, -0.01F, 2, 8, 0f, 0.02f),
        TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, 0.02F, -0.02F, -0.01F, 4, 6, 0.4f, 0.02f),
        MEATY(0.03F, 0.07F, 0.01F, 0.02F, -0.025F, 0.01F, -0.02F, -0.01F, 0, 2, 1.1f, 0.6f),
        MYTHICAL_SPEED(0.31F, 0.17F, 0.082F, 0.0375F, 0F, 0.1F, 0F, -0.01F, 0, 8, 0.5f, 0.02f),
        MYTHICAL_GLIDE(0.013F, 0.08F, 0.035F, 0.039F, 0F, 0.185F, 0F, -0.01F, 0, 6, 0.5f, 0.02f),
        MYTHICAL_TANK(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, 0.15F, -0.03F, -0.01F, 14, 6, 0.5f, 0.02f),
        MYTHICAL_ALL(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, 0.185F, -0.03F, -0.01F, 14, 6, 0.5f, 0.02f);

        private final ImmutableMap<MoaAttributes, Weighting> data;

        SpawnStatWeighting(float baseGroundSpeed, float groundSpeedVariance, float baseGlidingSpeed, float glidingSpeedVariance, float baseGlidingDecay, float glidingDecayVariance, float baseJumpStrength, float jumpStrengthVariance, float baseMaxHealth, float maxHealthVariance, float baseDropMultiplier, float maxDropMultiplierVariance) {
            var builder = ImmutableMap.<MoaAttributes, Weighting>builder();
            builder.put(MoaAttributes.GROUND_SPEED, new Weighting(baseGroundSpeed, groundSpeedVariance));
            builder.put(MoaAttributes.GLIDING_SPEED, new Weighting(baseGlidingSpeed, glidingSpeedVariance));
            builder.put(MoaAttributes.GLIDING_DECAY, new Weighting(baseGlidingDecay, glidingDecayVariance));
            builder.put(MoaAttributes.JUMPING_STRENGTH, new Weighting(baseJumpStrength, jumpStrengthVariance));
            builder.put(MoaAttributes.MAX_HEALTH, new Weighting(baseMaxHealth, maxHealthVariance));
            builder.put(MoaAttributes.DROP_MULTIPLIER, new Weighting(baseDropMultiplier, maxDropMultiplierVariance));
            data = builder.build();
        }

        @SuppressWarnings("ConstantConditions")
        public float configure(MoaAttributes attribute, MoaRace race, RandomSource random) {
            Weighting statData = data.get(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base + (random.nextFloat() * statData.variance) * (
                    race.defaultAffinity == attribute
                            ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F)
                            : 1F)));
        }

        private static record Weighting(float base, float variance) {
        }
    }

    public record MoaRace(MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting, boolean glowing, boolean legendary, ParticleType<?> particles) {
        public MoaRace(MoaAttributes defaultAffinity, SpawnStatWeighting statWeighting) {
            this(defaultAffinity, statWeighting, false, false, ParticleTypes.ENCHANT);
        }

        public ResourceLocation getId() {
            ResourceLocation key = MoaRaceRegistry.MOA_RACES.getKey(this);
            if (key != null) {
                return key;
            }
            ParadiseLost.LOG.error("MoaAPI.MoaRace.getId() called for an unregistered race. Report this to somebody.");

            return ModConstants.id("fallback");
        }

        public String getTranslationKey() {
            ResourceLocation id = this.getId();
            return "moa.race." + id.getNamespace() + "." + id.getPath();
        }
    }
}
