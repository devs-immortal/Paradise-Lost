package net.id.paradise_lost.registry;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.api.MoaAPI.SpawnStatWeighting;
import net.id.paradise_lost.registration.registries.DatapackRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public final class MoaSpawnStatWeightingRegistry {

    public static final DatapackRegistry<SpawnStatWeighting> REGISTRY =
            DatapackRegistry.<SpawnStatWeighting>builder(ModConstants.id("moa_spawn_stat_weighting"))
                    .withElementCodec(SpawnStatWeighting.DIRECT_CODEC)
                    .withNetworkCodec(SpawnStatWeighting.DIRECT_CODEC)
                    .withBootstrap(MoaSpawnStatWeightingRegistry::bootstrap)
                    .build();

    public static final ResourceKey<SpawnStatWeighting> SPEED = key("speed");
    public static final ResourceKey<SpawnStatWeighting> GLIDE = key("glide");
    public static final ResourceKey<SpawnStatWeighting> ENDURANCE = key("endurance");
    public static final ResourceKey<SpawnStatWeighting> TANK = key("tank");
    public static final ResourceKey<SpawnStatWeighting> MEATY = key("meaty");
    public static final ResourceKey<SpawnStatWeighting> MYTHICAL_SPEED = key("mythical_speed");
    public static final ResourceKey<SpawnStatWeighting> MYTHICAL_GLIDE = key("mythical_glide");
    public static final ResourceKey<SpawnStatWeighting> MYTHICAL_TANK = key("mythical_tank");
    public static final ResourceKey<SpawnStatWeighting> MYTHICAL_ALL = key("mythical_all");

    public static final SpawnStatWeighting TANK_VALUE = SpawnStatWeighting.of(
            0.0F, 0.07F, 0.01F, 0.02F, -0.025F, 0.02F, -0.02F, -0.01F, 4, 6, 0.4f, 0.02f);
    public static final Holder<SpawnStatWeighting> TANK_HOLDER = Holder.direct(TANK_VALUE);

    private MoaSpawnStatWeightingRegistry() {
    }

    public static void init() {
    }

    public static ResourceKey<SpawnStatWeighting> key(String path) {
        return ResourceKey.create(REGISTRY.key(), ModConstants.id(path));
    }

    public static Registry<SpawnStatWeighting> get(RegistryAccess access) {
        return REGISTRY.get(access);
    }

    public static Optional<SpawnStatWeighting> find(RegistryAccess access, ResourceLocation id) {
        if (id == null) {
            return Optional.empty();
        }
        return get(access).getOptional(id);
    }

    private static void bootstrap(BootstrapContext<SpawnStatWeighting> context) {
        context.register(SPEED, SpawnStatWeighting.of(0.08F, 0.1F, 0.02F, 0.03F, -0.02F, 0.2F, 0F, -0.01F, 0, 8, 0f, 0.02f));
        context.register(GLIDE, SpawnStatWeighting.of(0.013F, 0.08F, 0.035F, 0.039F, -0.01F, 0.05F, 0F, 0.005F, 0, 6, 0f, 0.02f));
        context.register(ENDURANCE, SpawnStatWeighting.of(0.023F, 0.06F, 0.02F, 0.02F, -0.02F, 0.04F, -0.01F, -0.01F, 2, 8, 0f, 0.02f));
        context.register(TANK, TANK_VALUE);
        context.register(MEATY, SpawnStatWeighting.of(0.03F, 0.07F, 0.01F, 0.02F, -0.025F, 0.01F, -0.02F, -0.01F, 0, 2, 1.1f, 0.6f));
        context.register(MYTHICAL_SPEED, SpawnStatWeighting.of(0.31F, 0.17F, 0.082F, 0.0375F, 0F, 0.1F, 0F, -0.01F, 0, 8, 0.5f, 0.02f));
        context.register(MYTHICAL_GLIDE, SpawnStatWeighting.of(0.013F, 0.08F, 0.035F, 0.039F, 0F, 0.185F, 0F, -0.01F, 0, 6, 0.5f, 0.02f));
        context.register(MYTHICAL_TANK, SpawnStatWeighting.of(0.0F, 0.07F, 0.01F, 0.02F, -0.025F, 0.15F, -0.03F, -0.01F, 14, 6, 0.5f, 0.02f));
        context.register(MYTHICAL_ALL, SpawnStatWeighting.of(0.31F, 0.17F, 0.035F, 0.039F, -0.085F, 0.185F, -0.03F, -0.01F, 14, 6, 0.5f, 0.02f));
    }
}
