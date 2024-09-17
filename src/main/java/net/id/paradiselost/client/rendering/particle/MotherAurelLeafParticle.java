package net.id.paradiselost.client.rendering.particle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.SimpleParticleType;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

import java.util.SplittableRandom;

//I did not steal this from that one falling leaves mod, you have no proof!
@Environment(EnvType.CLIENT)
public class MotherAurelLeafParticle extends SpriteBillboardParticle {

    private static final SplittableRandom random = new SplittableRandom();
    private final float rotateFactor;
    private final double velocityComposite, velocityDown;

    protected MotherAurelLeafParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider provider) {
        super(clientWorld, d, e, f);
        this.setSprite(provider);

        this.collidesWithWorld = true;
        this.gravityStrength = 0.09F;
        this.maxAge = 1200;

        this.velocityX *= 0.325F;
        this.velocityY *= 0.0F;
        this.velocityZ *= 0.325F;

        this.velocityComposite = g / 50;
        velocityDown = h;

        this.rotateFactor = ((float) Math.random() - 0.5F) * 0.002F;
        this.scale = (float) (0.06 + (random.nextDouble() / 14));
    }

    public void tick() {
        velocityY = velocityDown;
        super.tick();
        this.velocityZ = velocityComposite / 2;
        this.velocityX = velocityComposite / 2;
        if (this.age < 2) {
            this.velocityY = 0;
        }
        if (this.age > this.maxAge - 1 / 0.06F) {
            if (this.alpha > 0.06F) {
                this.alpha -= 0.06F;
            } else {
                this.markDead();
            }
        }
        this.prevAngle = this.angle;
        if (!this.onGround && !this.world.getFluidState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).isIn(FluidTags.WATER)) {
            this.angle += (float) (Math.PI * Math.sin(this.rotateFactor * this.age) / 2);
        }
        if (this.world.getFluidState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).isIn(FluidTags.WATER)) {
            this.velocityY = 0;
            this.gravityStrength = 0;
        } else {
            this.gravityStrength = 0.1F;
        }
    }

    @Override
    public int getBrightness(float tint) {
        return 200;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<SimpleParticleType> {
        private final SpriteProvider provider;

        public DefaultFactory(SpriteProvider provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new MotherAurelLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.provider);
        }
    }
}
