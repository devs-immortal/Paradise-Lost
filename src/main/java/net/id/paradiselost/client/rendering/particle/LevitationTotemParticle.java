package net.id.paradiselost.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.AnimatedParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class LevitationTotemParticle extends AnimatedParticle {
    LevitationTotemParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25F);
        this.velocityMultiplier = 0.6F;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.scale *= 0.75F;
        this.maxAge = 60 + this.random.nextInt(12);
        this.setSpriteForAge(spriteProvider);
        if (this.random.nextInt(4) == 0) {
            this.setColor(0.6F + this.random.nextFloat() * 0.2F, 0.4F + this.random.nextFloat() * 0.3F, 0.7F + this.random.nextFloat() * 0.3F);
        } else {
            this.setColor(0.17F + this.random.nextFloat() * 0.2F, 0.25F + this.random.nextFloat() * 0.3F, 0.7F + this.random.nextFloat() * 0.2F);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            return new LevitationTotemParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
