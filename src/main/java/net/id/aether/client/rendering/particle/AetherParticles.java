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

@Environment(EnvType.CLIENT)
public class AetherParticles {
    public static DefaultParticleType GOLDEN_OAK_LEAF = register("golden_leaf", GoldenOakLeafParticle.DefaultFactory::new);
    public static DefaultParticleType FALLING_ORANGE_PETAL = register("falling_orange_petal", FallingOrangePetalParticle.DefaultFactory::new);
    public static DefaultParticleType VENOM_BUBBLE = register("venom_bubble", VenomBubbleParticle.DefaultFactory::new);
    /**
     * A super fancy RGB splash particle.
     */
    public static ParticleType<ColoredSplashParticleEffect> COLORED_SPLASH = registerComplex("colored_splash", ColoredSplashParticleEffect.FACTORY, ColoredSplashParticle.FACTORY);

    private static DefaultParticleType register(String id, PendingParticleFactory<DefaultParticleType> factory){
        DefaultParticleType particle = Registry.register(Registry.PARTICLE_TYPE, Aether.locate(id), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(particle, factory);
        return particle;
    }
    
    /**
     * Registers a complex particle type.
     *
     * TODO Verify that this is the correct way of doing this, it's late here.
     *
     * @param id The name of the new particle
     * @param factory The common factory
     * @param clientFactory The client only factory
     * @param <T> The type of the effect
     * @return The new type
     */
    private static <T extends ParticleEffect> ParticleType<T> registerComplex(String id, ParticleEffect.Factory<T> factory, PendingParticleFactory<T> clientFactory){
        var type = Registry.register(Registry.PARTICLE_TYPE, Aether.locate(id), FabricParticleTypes.complex(true, factory));
        ParticleFactoryRegistry.getInstance().register(type, clientFactory);
        return type;
    }

    public static void initClient() {
        // So sad... so empty...
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
}
