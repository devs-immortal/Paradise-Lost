package net.id.paradise_lost.api;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.component.MoaGenes;
import net.id.paradise_lost.entity.passive.moa.MoaAttributes;
import net.id.paradise_lost.registry.MoaBreedingRegistry;
import net.id.paradise_lost.registry.MoaRaceRegistry;
import net.id.paradise_lost.registry.MoaSpawnRegistry;
import net.id.paradise_lost.registry.MoaSpawnStatWeightingRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class MoaAPI {

    public static Optional<MoaRace> findRace(RegistryAccess access, ResourceLocation raceId) {
        return MoaRaceRegistry.find(access, raceId);
    }

    public static MoaRace getRace(RegistryAccess access, @Nullable ResourceLocation raceId) {
        return MoaRaceRegistry.getOrFallback(access, raceId);
    }

    public static MoaRace getRace(Level level, @Nullable ResourceLocation raceId) {
        return getRace(level.registryAccess(), raceId);
    }

    public static MoaRace getFallbackRace(RegistryAccess access) {
        return MoaRaceRegistry.fallback(access);
    }

    public static Stream<ResourceLocation> getRegisteredRaceIds(RegistryAccess access) {
        return MoaRaceRegistry.raceIds(access);
    }

    @NotNull
    public static MoaRace getMoaFromSpawning(Level world, ResourceKey<Biome> biome, RandomSource random) {
        return MoaSpawnRegistry.getMoaFromSpawning(world, biome, random);
    }

    public static ResourceLocation getMoaRaceIdFromBreeding(MoaBreedingContext ctx) {
        return MoaBreedingRegistry.getMoaRaceIdFromBreeding(ctx);
    }

    public static ResourceLocation getMoaRaceIdFromBreeding(MoaGenes parentA, MoaGenes parentB, Level world, BlockPos pos) {
        return getMoaRaceIdFromBreeding(new MoaBreedingContext(parentA, parentB, world, pos));
    }

    public record MoaBreedingContext(MoaGenes parentA, MoaGenes parentB, Level world, BlockPos pos) {
    }

    public record AttributeWeighting(float base, float variance) {
        public static final Codec<AttributeWeighting> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.FLOAT.fieldOf("base").forGetter(AttributeWeighting::base),
                Codec.FLOAT.fieldOf("variance").forGetter(AttributeWeighting::variance)
        ).apply(instance, AttributeWeighting::new));
    }

    /**
     * Datapack-defined spawn-stat curve. Addons can register new profiles and point races at them.
     */
    public record SpawnStatWeighting(
            AttributeWeighting groundSpeed,
            AttributeWeighting glidingSpeed,
            AttributeWeighting glidingDecay,
            AttributeWeighting jumpingStrength,
            AttributeWeighting maxHealth,
            AttributeWeighting dropMultiplier
    ) {
        public static final Codec<SpawnStatWeighting> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance.group(
                AttributeWeighting.CODEC.fieldOf("ground_speed").forGetter(SpawnStatWeighting::groundSpeed),
                AttributeWeighting.CODEC.fieldOf("gliding_speed").forGetter(SpawnStatWeighting::glidingSpeed),
                AttributeWeighting.CODEC.fieldOf("gliding_decay").forGetter(SpawnStatWeighting::glidingDecay),
                AttributeWeighting.CODEC.fieldOf("jumping_strength").forGetter(SpawnStatWeighting::jumpingStrength),
                AttributeWeighting.CODEC.fieldOf("max_health").forGetter(SpawnStatWeighting::maxHealth),
                AttributeWeighting.CODEC.fieldOf("drop_multiplier").forGetter(SpawnStatWeighting::dropMultiplier)
        ).apply(instance, SpawnStatWeighting::new));

        public static SpawnStatWeighting of(
                float groundSpeedBase, float groundSpeedVariance,
                float glidingSpeedBase, float glidingSpeedVariance,
                float glidingDecayBase, float glidingDecayVariance,
                float jumpingStrengthBase, float jumpingStrengthVariance,
                float maxHealthBase, float maxHealthVariance,
                float dropMultiplierBase, float dropMultiplierVariance
        ) {
            return new SpawnStatWeighting(
                    new AttributeWeighting(groundSpeedBase, groundSpeedVariance),
                    new AttributeWeighting(glidingSpeedBase, glidingSpeedVariance),
                    new AttributeWeighting(glidingDecayBase, glidingDecayVariance),
                    new AttributeWeighting(jumpingStrengthBase, jumpingStrengthVariance),
                    new AttributeWeighting(maxHealthBase, maxHealthVariance),
                    new AttributeWeighting(dropMultiplierBase, dropMultiplierVariance)
            );
        }

        public AttributeWeighting forAttribute(MoaAttributes attribute) {
            return switch (attribute) {
                case GROUND_SPEED -> groundSpeed;
                case GLIDING_SPEED -> glidingSpeed;
                case GLIDING_DECAY -> glidingDecay;
                case JUMPING_STRENGTH -> jumpingStrength;
                case MAX_HEALTH -> maxHealth;
                case DROP_MULTIPLIER -> dropMultiplier;
            };
        }

        public float configure(MoaAttributes attribute, MoaRace race, RandomSource random) {
            AttributeWeighting statData = forAttribute(attribute);
            return Math.min(attribute.max, attribute.min + (statData.base() + (random.nextFloat() * statData.variance()) * (
                    race.defaultAffinity() == attribute
                            ? (attribute == MoaAttributes.DROP_MULTIPLIER ? 2F : 1.05F)
                            : 1F)));
        }
    }

    /**
     * Datapack-defined moa race. Texture defaults to {@code <namespace>:textures/entity/moa/<path>.png}
     * unless {@code texture} is set explicitly — addons can ship races under their own namespace.
     */
    public record MoaRace(
            MoaAttributes defaultAffinity,
            Holder<SpawnStatWeighting> statWeighting,
            boolean glowing,
            boolean legendary,
            ParticleType<?> particles,
            Optional<ResourceLocation> texture
    ) {
        public static final Codec<MoaRace> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                MoaAttributes.CODEC.fieldOf("default_affinity").forGetter(MoaRace::defaultAffinity),
                RegistryFileCodec.create(
                        MoaSpawnStatWeightingRegistry.REGISTRY.key(),
                        SpawnStatWeighting.DIRECT_CODEC
                ).fieldOf("stat_weighting").forGetter(MoaRace::statWeighting),
                Codec.BOOL.optionalFieldOf("glowing", false).forGetter(MoaRace::glowing),
                Codec.BOOL.optionalFieldOf("legendary", false).forGetter(MoaRace::legendary),
                BuiltInRegistries.PARTICLE_TYPE.byNameCodec().optionalFieldOf("particles", ParticleTypes.ENCHANT).forGetter(MoaRace::particles),
                ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(MoaRace::texture)
        ).apply(instance, MoaRace::new));

        public MoaRace(MoaAttributes defaultAffinity, Holder<SpawnStatWeighting> statWeighting) {
            this(defaultAffinity, statWeighting, false, false, ParticleTypes.ENCHANT, Optional.empty());
        }

        public MoaRace(MoaAttributes defaultAffinity, Holder<SpawnStatWeighting> statWeighting, boolean glowing, boolean legendary, ParticleType<?> particles) {
            this(defaultAffinity, statWeighting, glowing, legendary, particles, Optional.empty());
        }

        public SpawnStatWeighting weighting() {
            return statWeighting.value();
        }

        /**
         * Resolves the entity texture for this race id.
         * Default: {@code namespace:textures/entity/moa/path.png}
         */
        public ResourceLocation textureFor(ResourceLocation raceId) {
            return texture.orElseGet(() -> ResourceLocation.fromNamespaceAndPath(
                    raceId.getNamespace(), "textures/entity/moa/" + raceId.getPath() + ".png"));
        }

        public String translationKey(ResourceLocation raceId) {
            return "moa.race." + raceId.getNamespace() + "." + raceId.getPath();
        }
    }
}
