package net.id.paradise_lost.particle;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public final class ParadiseLostParticleTypes {
    public static final RegistrationProvider<ParticleType<?>> PARTICLE_TYPES =
            RegistrationProvider.get(Registries.PARTICLE_TYPE, ModConstants.MODID);

    public static final SimpleParticleType CHERINE_FLAME = register("cherine_flame");
    public static final SimpleParticleType MOTHER_AUREL_LEAF = register("golden_leaf");
    public static final SimpleParticleType LEVITA_BLOOP = register("levita_bloop");
    public static final SimpleParticleType LEVITATION_TOTEM = register("levitation_totem");
    public static final SimpleParticleType LIT_CLOUD = register("lit_cloud");

    private ParadiseLostParticleTypes() {}

    public static void init() {}

    private static SimpleParticleType register(String id) {
        SimpleParticleType type = new SimpleParticleType(true);
        PARTICLE_TYPES.register(id, () -> type);
        return type;
    }
}
