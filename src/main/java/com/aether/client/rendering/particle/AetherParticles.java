package com.aether.client.rendering.particle;

import com.aether.Aether;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;

public class AetherParticles {

    public static final SimpleParticleType GOLDEN_OAK_LEAF;

    static {
        GOLDEN_OAK_LEAF = Registry.register(Registry.PARTICLE_TYPE, Aether.locate("golden_leaf"), FabricParticleTypes.simple(true));
    }

    public static void init() {
    }

    @Environment(EnvType.CLIENT)
    public static void initClient() {
        ParticleFactoryRegistry.getInstance().register(GOLDEN_OAK_LEAF, GoldenOakLeafParticle.DefaultFactory::new);
    }
}
