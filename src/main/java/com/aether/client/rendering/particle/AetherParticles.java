package com.aether.client.rendering.particle;

import com.aether.Aether;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.registry.Registry;

public class AetherParticles {

    public static final DefaultParticleType GOLDEN_OAK_LEAF;

    static {
        GOLDEN_OAK_LEAF = Registry.register(Registry.PARTICLE_TYPE, Aether.locate("golden_leaf"), new DefaultParticleType(true));
    }
}
