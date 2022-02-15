package net.id.aether.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.id.aether.Aether;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.registry.Registry;

/**
 * The server/common parts of the Aether particle system.
 * <p>
 * These are safe to use on the server, even though the package name says otherwise.
 * <p>
 * FIXME Refactor this for naming issues.
 */
public class AetherParticles {
    public static DefaultParticleType GOLDEN_OAK_LEAF = register("golden_leaf");
    public static DefaultParticleType FALLING_ORANGE_PETAL = register("falling_orange_petal");
    public static DefaultParticleType VENOM_BUBBLE = register("venom_bubble");
    /**
     * A super fancy RGB splash particle.
     */
    public static ParticleType<ColoredSplashParticleEffect> COLORED_SPLASH = register("colored_splash", ColoredSplashParticleEffect.FACTORY);
    
    /**
     * Registers a simple particle type.
     *
     * @param id THe name of the new particle
     * @return The new type
     */
    private static DefaultParticleType register(String id) {
        return Registry.register(Registry.PARTICLE_TYPE, Aether.locate(id), FabricParticleTypes.simple(true));
    }
    
    /**
     * Registers a complex particle type.
     *
     * @param id      The name of the new particle
     * @param factory The common factory
     * @param <T>     The type of the effect
     * @return The new type
     */
    private static <T extends ParticleEffect> ParticleType<T> register(String id, ParticleEffect.Factory<T> factory) {
        return Registry.register(Registry.PARTICLE_TYPE, Aether.locate(id), FabricParticleTypes.complex(true, factory));
    }
    
    /**
     * Ensures that clinit ran.
     */
    public static void init() {
    }
    
    /**
     * Creates a colored particle effect.
     *
     * @param color The color to use
     * @return The new effect
     */
    public static ColoredSplashParticleEffect coloredSplash(int color) {
        return new ColoredSplashParticleEffect(COLORED_SPLASH, color);
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
            register(GOLDEN_OAK_LEAF, GoldenOakLeafParticle.DefaultFactory::new);
            register(FALLING_ORANGE_PETAL, FallingOrangePetalParticle.DefaultFactory::new);
            register(VENOM_BUBBLE, VenomBubbleParticle.DefaultFactory::new);
            register(COLORED_SPLASH, ColoredSplashParticle.FACTORY);
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
