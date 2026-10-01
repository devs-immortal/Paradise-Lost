package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.api.MoaAPI.MoaRace;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.registration.registries.RegistryFeatureType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;

import static net.id.paradise_lost.api.MoaAPI.SpawnStatWeighting.*;
import static net.id.paradise_lost.entity.passive.moa.MoaAttributes.*;

public class MoaRaceRegistry {

    public static final ResourceLocation REGISTRY_ID = ModConstants.id("moa_race");

    private static final RegistrationProvider<MoaRace> RACES =
            RegistrationProvider.get(REGISTRY_ID, ModConstants.MODID);

    public static final Registry<MoaRace> MOA_RACES = RACES.registryBuilder()
            .withFeature(RegistryFeatureType.SYNCED)
            .withFeature(RegistryFeatureType.DEFAULTED, ModConstants.id("fallback"))
            .build();

    public static final RegistryObject<MoaRace, MoaRace> HIGHLANDS_BLUE =
            register("highlands_blue", new MoaRace(DROP_MULTIPLIER, MEATY));
    public static final RegistryObject<MoaRace, MoaRace> HIGHLANDS_CYAN =
            register("highlands_cyan", new MoaRace(GLIDING_DECAY, MEATY));
    public static final RegistryObject<MoaRace, MoaRace> GOLDENROD =
            register("goldenrod", new MoaRace(GROUND_SPEED, ENDURANCE));
    public static final RegistryObject<MoaRace, MoaRace> MINTGRASS =
            register("mintgrass", new MoaRace(GLIDING_SPEED, SPEED));
    public static final RegistryObject<MoaRace, MoaRace> TANGERINE =
            register("tangerine", new MoaRace(JUMPING_STRENGTH, SPEED));

    public static final RegistryObject<MoaRace, MoaRace> STRAWBERRY_WISTAR =
            register("strawberry_wistar", new MoaRace(GLIDING_SPEED, SPEED));
    public static final RegistryObject<MoaRace, MoaRace> BLACKCURRANT_WISTAR =
            register("blackcurrant_wistar", new MoaRace(GLIDING_DECAY, GLIDE));
    public static final RegistryObject<MoaRace, MoaRace> GREYHOUND =
            register("greyhound", new MoaRace(GROUND_SPEED, ENDURANCE));
    public static final RegistryObject<MoaRace, MoaRace> FROSTGRASS =
            register("frostgrass", new MoaRace(JUMPING_STRENGTH, TANK));

    public static final RegistryObject<MoaRace, MoaRace> FOXTROT =
            register("foxtrot", new MoaRace(GLIDING_DECAY, GLIDE));
    public static final RegistryObject<MoaRace, MoaRace> SCARLET =
            register("scarlet", new MoaRace(GROUND_SPEED, SPEED));
    public static final RegistryObject<MoaRace, MoaRace> REDHOOD =
            register("redhood", new MoaRace(MAX_HEALTH, TANK));

    public static final RegistryObject<MoaRace, MoaRace> MOONSTRUCK =
            register("moonstruck", new MoaRace(GLIDING_SPEED, GLIDE, true, true, ParticleTypes.GLOW));

    public static final RegistryObject<MoaRace, MoaRace> GREENSEED =
            register("greenseed", new MoaRace(JUMPING_STRENGTH, ENDURANCE));
    public static final RegistryObject<MoaRace, MoaRace> AQUILAN =
            register("aquilan", new MoaRace(GLIDING_SPEED, SPEED));

    public static final RegistryObject<MoaRace, MoaRace> FALLBACK_MOA =
            register("fallback", new MoaRace(GROUND_SPEED, TANK));

    private static RegistryObject<MoaRace, MoaRace> register(String name, MoaRace race) {
        return RACES.register(name, () -> race);
    }

    public static void init() {
    }
}
