package com.aether.client.rendering.particle;

import com.aether.Aether;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class AetherParticles {

    public static final DefaultParticleType GOLDEN_OAK_LEAF;

    static {
        GOLDEN_OAK_LEAF = Registry.register(Registry.PARTICLE_TYPE, Aether.locate("golden_leaf"), new DefaultParticleType(true));
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ParticleManager manager = MinecraftClient.getInstance().particleManager;

        manager.registerFactory(GOLDEN_OAK_LEAF, GoldenOakLeafParticle.DefaultFactory::new);
    }
}
