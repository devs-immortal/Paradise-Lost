package net.id.paradiselost.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

/**
 * The server/common parts of the Paradise Lost particle system.
 * <p>
 * These are safe to use on the server, even though the package name says otherwise.
 * <p>
 * FIXME Refactor this for naming issues.
 */
public class ParadiseLostParticles {
    public static SimpleParticleType CHERINE_FLAME = register("cherine_flame");
    public static SimpleParticleType FALLING_ORANGE_PETAL = register("falling_orange_petal");
    public static SimpleParticleType MOTHER_AUREL_LEAF = register("golden_leaf");
    public static SimpleParticleType LEVITA_BLOOP = register("levita_bloop");
    public static SimpleParticleType LEVITATION_TOTEM = register("levitation_totem");
    public static SimpleParticleType LIT_CLOUD = register("lit_cloud");
    
    /**
     * Registers a simple particle type.
     *
     * @param id THe name of the new particle
     * @return The new type
     */
    private static SimpleParticleType register(String id) {
        return Registry.register(Registries.PARTICLE_TYPE, ParadiseLost.locate(id), FabricParticleTypes.simple(true));
    }

    /**
     * Ensures that clinit ran.
     */
    public static void init() {
    }

    /**
     * The client half of the particle system.
     */
    @Environment(EnvType.CLIENT)
    public static final class Client {
        /**
         * Registers the client half of this.
         */
        public static void init() {
            register(CHERINE_FLAME, CherineFlameParticle.DefaultFactory::new);
            register(FALLING_ORANGE_PETAL, FallingOrangePetalParticle.DefaultFactory::new);
            register(MOTHER_AUREL_LEAF, MotherAurelLeafParticle.DefaultFactory::new);
            register(LEVITA_BLOOP, LevitaBloopParticle.DefaultFactory::new);
            register(LEVITATION_TOTEM, LevitationTotemParticle.DefaultFactory::new);
            register(LIT_CLOUD, LitCloudParticle.DefaultFactory::new);
        }

        /**
         * Registers the client side of a particle.
         *
         * @param particle The common particle type
         * @param factory  The client factory
         * @param <T>      The generic type of the particle
         */
        private static <T extends ParticleEffect> void register(ParticleType<T> particle, PendingParticleFactory<T> factory) {
            ParticleFactoryRegistry.getInstance().register(particle, factory);
        }
    }
}
