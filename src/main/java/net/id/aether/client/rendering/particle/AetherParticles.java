package net.id.aether.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry.PendingParticleFactory;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.id.aether.Aether;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

@Environment(EnvType.CLIENT)
public class AetherParticles {
    public static DefaultParticleType GOLDEN_OAK_LEAF = register("golden_leaf", GoldenOakLeafParticle.DefaultFactory::new);
    public static DefaultParticleType FALLING_ORANGE_PETAL = register("falling_orange_petal", FallingOrangePetalParticle.DefaultFactory::new);
    public static DefaultParticleType VENOM_BUBBLE= register("venom_bubble", VenomBubbleParticle.DefaultFactory::new);
    public static DefaultParticleType BLUE_SPLASH = register("blue_splash", provider -> new ColoredSplashParticle.SplashFactory(provider, 0x60899E));
    public static DefaultParticleType GOLD_SPLASH = register("gold_splash", provider -> new ColoredSplashParticle.SplashFactory(provider, 0x918737));
    public static DefaultParticleType PURPLE_SPLASH = register("purple_splash", provider -> new ColoredSplashParticle.SplashFactory(provider, 0x644E7B));
    public static DefaultParticleType WHITE_SPLASH = register("white_splash", provider -> new ColoredSplashParticle.SplashFactory(provider, 0x8E8F8F));

    private static DefaultParticleType register(String id, PendingParticleFactory<DefaultParticleType> factory){
        DefaultParticleType particle = Registry.register(Registry.PARTICLE_TYPE, Aether.locate(id), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(particle, factory);
        return particle;
    }

    public static void initClient() {
        // So sad... so empty...
    }
}
