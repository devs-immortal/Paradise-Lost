package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.api.MoaAPI.MoaRace;
import net.id.paradise_lost.api.MoaAPI.SpawnStatWeighting;
import net.id.paradise_lost.registration.registries.DatapackRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.stream.Stream;

import static net.id.paradise_lost.entity.passive.moa.MoaAttributes.*;

public final class MoaRaceRegistry {

    public static final DatapackRegistry<MoaRace> REGISTRY =
            DatapackRegistry.<MoaRace>builder(ModConstants.id("moa_race"))
                    .withElementCodec(MoaRace.CODEC)
                    .withNetworkCodec(MoaRace.CODEC)
                    .withBootstrap(MoaRaceRegistry::bootstrap)
                    .build();

    public static final ResourceLocation FALLBACK_ID = ModConstants.id("fallback");
    public static final ResourceKey<MoaRace> FALLBACK = key("fallback");

    public static final ResourceKey<MoaRace> HIGHLANDS_BLUE = key("highlands_blue");
    public static final ResourceKey<MoaRace> HIGHLANDS_CYAN = key("highlands_cyan");
    public static final ResourceKey<MoaRace> GOLDENROD = key("goldenrod");
    public static final ResourceKey<MoaRace> MINTGRASS = key("mintgrass");
    public static final ResourceKey<MoaRace> TANGERINE = key("tangerine");
    public static final ResourceKey<MoaRace> STRAWBERRY_WISTAR = key("strawberry_wistar");
    public static final ResourceKey<MoaRace> BLACKCURRANT_WISTAR = key("blackcurrant_wistar");
    public static final ResourceKey<MoaRace> GREYHOUND = key("greyhound");
    public static final ResourceKey<MoaRace> FROSTGRASS = key("frostgrass");
    public static final ResourceKey<MoaRace> FOXTROT = key("foxtrot");
    public static final ResourceKey<MoaRace> SCARLET = key("scarlet");
    public static final ResourceKey<MoaRace> REDHOOD = key("redhood");
    public static final ResourceKey<MoaRace> MOONSTRUCK = key("moonstruck");
    public static final ResourceKey<MoaRace> GREENSEED = key("greenseed");
    public static final ResourceKey<MoaRace> AQUILAN = key("aquilan");

    /** Used before registry access is available (genes field init, missing lookups). */
    public static final MoaRace FALLBACK_VALUE = new MoaRace(
            GROUND_SPEED, MoaSpawnStatWeightingRegistry.TANK_HOLDER, false, false, ParticleTypes.ENCHANT, Optional.empty());

    private MoaRaceRegistry() {
    }

    public static void init() {
    }

    public static ResourceKey<MoaRace> key(String path) {
        return ResourceKey.create(REGISTRY.key(), ModConstants.id(path));
    }

    public static Registry<MoaRace> get(RegistryAccess access) {
        return REGISTRY.get(access);
    }

    public static Optional<MoaRace> find(RegistryAccess access, ResourceLocation id) {
        if (id == null) {
            return Optional.empty();
        }
        return get(access).getOptional(id);
    }

    public static MoaRace getOrFallback(RegistryAccess access, ResourceLocation id) {
        return find(access, id).orElseGet(() -> get(access).getOptional(FALLBACK_ID).orElse(FALLBACK_VALUE));
    }

    public static MoaRace fallback(RegistryAccess access) {
        return get(access).getOptional(FALLBACK_ID).orElse(FALLBACK_VALUE);
    }

    public static boolean isFallback(ResourceLocation raceId) {
        return FALLBACK_ID.equals(raceId);
    }

    public static Stream<ResourceLocation> raceIds(RegistryAccess access) {
        return get(access).keySet().stream();
    }

    private static void bootstrap(BootstrapContext<MoaRace> context) {
        var weightings = context.lookup(MoaSpawnStatWeightingRegistry.REGISTRY.key());
        Holder.Reference<SpawnStatWeighting> speed = weightings.getOrThrow(MoaSpawnStatWeightingRegistry.SPEED);
        Holder.Reference<SpawnStatWeighting> glide = weightings.getOrThrow(MoaSpawnStatWeightingRegistry.GLIDE);
        Holder.Reference<SpawnStatWeighting> endurance = weightings.getOrThrow(MoaSpawnStatWeightingRegistry.ENDURANCE);
        Holder.Reference<SpawnStatWeighting> tank = weightings.getOrThrow(MoaSpawnStatWeightingRegistry.TANK);
        Holder.Reference<SpawnStatWeighting> meaty = weightings.getOrThrow(MoaSpawnStatWeightingRegistry.MEATY);

        register(context, HIGHLANDS_BLUE, new MoaRace(DROP_MULTIPLIER, meaty));
        register(context, HIGHLANDS_CYAN, new MoaRace(GLIDING_DECAY, meaty));
        register(context, GOLDENROD, new MoaRace(GROUND_SPEED, endurance));
        register(context, MINTGRASS, new MoaRace(GLIDING_SPEED, speed));
        register(context, TANGERINE, new MoaRace(JUMPING_STRENGTH, speed));
        register(context, STRAWBERRY_WISTAR, new MoaRace(GLIDING_SPEED, speed));
        register(context, BLACKCURRANT_WISTAR, new MoaRace(GLIDING_DECAY, glide));
        register(context, GREYHOUND, new MoaRace(GROUND_SPEED, endurance));
        register(context, FROSTGRASS, new MoaRace(JUMPING_STRENGTH, tank));
        register(context, FOXTROT, new MoaRace(GLIDING_DECAY, glide));
        register(context, SCARLET, new MoaRace(GROUND_SPEED, speed));
        register(context, REDHOOD, new MoaRace(MAX_HEALTH, tank));
        register(context, MOONSTRUCK, new MoaRace(GLIDING_SPEED, glide, true, true, ParticleTypes.GLOW, Optional.empty()));
        register(context, GREENSEED, new MoaRace(JUMPING_STRENGTH, endurance));
        register(context, AQUILAN, new MoaRace(GLIDING_SPEED, speed));
        register(context, FALLBACK, new MoaRace(GROUND_SPEED, tank, false, false, ParticleTypes.ENCHANT, Optional.empty()));
    }

    private static void register(BootstrapContext<MoaRace> context, ResourceKey<MoaRace> key, MoaRace race) {
        context.register(key, race);
    }
}
