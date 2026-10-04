package net.id.paradise_lost.client.rendering.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SimpleAnimatedParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

public class LevitationTotemParticle extends SimpleAnimatedParticle {
    LevitationTotemParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, spriteProvider, 1.25F);
        this.friction = 0.6F;
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
        this.quadSize *= 0.75F;
        this.lifetime = 60 + this.random.nextInt(12);
        this.setSpriteFromAge(spriteProvider);
        if (this.random.nextInt(4) == 0) {
            this.setColor(0.6F + this.random.nextFloat() * 0.2F, 0.4F + this.random.nextFloat() * 0.3F, 0.7F + this.random.nextFloat() * 0.3F);
        } else {
            this.setColor(0.17F + this.random.nextFloat() * 0.2F, 0.25F + this.random.nextFloat() * 0.3F, 0.7F + this.random.nextFloat() * 0.2F);
        }
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public DefaultFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new LevitationTotemParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
