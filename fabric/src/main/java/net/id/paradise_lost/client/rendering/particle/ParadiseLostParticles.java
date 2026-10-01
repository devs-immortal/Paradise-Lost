package net.id.paradise_lost.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.id.paradise_lost.particle.ParadiseLostParticleTypes;

@Environment(EnvType.CLIENT)
public final class ParadiseLostParticles {
    private ParadiseLostParticles() {}

    public static void initClient() {
        ParticleFactoryRegistry.getInstance().register(ParadiseLostParticleTypes.CHERINE_FLAME, CherineFlameParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParadiseLostParticleTypes.MOTHER_AUREL_LEAF, MotherAurelLeafParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParadiseLostParticleTypes.LEVITA_BLOOP, LevitaBloopParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParadiseLostParticleTypes.LEVITATION_TOTEM, LevitationTotemParticle.DefaultFactory::new);
        ParticleFactoryRegistry.getInstance().register(ParadiseLostParticleTypes.LIT_CLOUD, LitCloudParticle.DefaultFactory::new);
    }
}
