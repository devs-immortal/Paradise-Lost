package net.id.paradiselost.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.particle.SuspendParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;

/*
 * Basically just a SuspendParticle, but that particle isn't public :I
 * These are bigger than normal suspend particles though
 */
public class LevitaBloopParticle extends SpriteBillboardParticle {

    LevitaBloopParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
        super(clientWorld, d, e, f, g, h, i);
        float j = this.random.nextFloat() * 0.1F + 0.2F;
        this.red = j;
        this.green = j;
        this.blue = j;
        this.setBoundingBoxSpacing(0.02F, 0.02F);
        this.scale = this.scale * (this.random.nextFloat() * 0.7F + 1.1F);
        this.velocityX *= 0.02F;
        this.velocityY *= 0.02F;
        this.velocityZ *= 0.02F;
        this.maxAge = (int)(20.0 / (Math.random() * 0.8 + 0.2));
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void move(double dx, double dy, double dz) {
        this.setBoundingBox(this.getBoundingBox().offset(dx, dy, dz));
        this.repositionFromBoundingBox();
    }

    @Override
    public void tick() {
        this.prevPosX = this.x;
        this.prevPosY = this.y;
        this.prevPosZ = this.z;
        if (this.maxAge-- <= 0) {
            this.markDead();
        } else {
            this.move(this.velocityX, this.velocityY, this.velocityZ);
            this.velocityX *= 0.99;
            this.velocityY *= 0.99;
            this.velocityZ *= 0.99;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider spriteProvider;

        public DefaultFactory(SpriteProvider spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
            LevitaBloopParticle suspendParticle = new LevitaBloopParticle(clientWorld, d, e, f, g, h, i);
            suspendParticle.setColor(0.5F, 0.45F, 1.0F);
            suspendParticle.setSprite(this.spriteProvider);
            suspendParticle.setAlpha(1.0F - clientWorld.random.nextFloat() * 0.7F);
            suspendParticle.setMaxAge(suspendParticle.getMaxAge() / 2);
            return suspendParticle;
        }
    }
}
