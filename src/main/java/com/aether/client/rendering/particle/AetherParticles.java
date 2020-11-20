package com.aether.client.rendering.particle;

import com.aether.Aether;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class AetherParticles {

    public static DefaultParticleType GOLDEN_OAK_LEAF;

    public static void initializeClient() {
        GOLDEN_OAK_LEAF = Registry.register(Registry.PARTICLE_TYPE, Aether.locate("golden_leaf"), FabricParticleTypes.simple(true));
        ParticleFactoryRegistry.getInstance().register(GOLDEN_OAK_LEAF, GoldenOakLeafParticle.DefaultFactory::new);
    }
}
