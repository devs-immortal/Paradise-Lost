package com.aether.client.rendering.particle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;

import java.util.Random;
import java.util.SplittableRandom;

//I did not steal this from that one falling leaves mod, you have no proof!
public class GoldenOakLeafParticle extends SpriteBillboardParticle {

    private final float rotateFactor;
    private static final SplittableRandom random = new SplittableRandom();
    private final double velocityComposite, velocityDown;

    protected GoldenOakLeafParticle(ClientWorld clientWorld, double d, double e, double f, double g, double h, double i, SpriteProvider provider) {
        super(clientWorld, d, e, f);
        this.setSprite(provider);

        this.collidesWithWorld = true;
        this.gravityStrength = 0.1F;
        this.maxAge = 400;

        this.velocityX *= 0.3F;
        this.velocityY *= 0.0F;
        this.velocityZ *= 0.3F;

        this.velocityComposite = g / 30;
        velocityDown = h;

        this.rotateFactor = ((float)Math.random() - 0.5F) * 0.01F;
        this.scale = (float) (0.08 + (random.nextDouble() / 10));
    }

    public void tick() {
        velocityY = velocityDown;
        super.tick();
        this.velocityZ = velocityComposite / 2;
        this.velocityX = velocityComposite / 2;
        if (this.age < 2) {
            this.velocityY = 0;
        }
        if (this.age > this.maxAge - 1/0.06F) {
            if (this.colorAlpha > 0.06F) {
                this.colorAlpha -= 0.06F;
            } else {
                this.markDead();
            }
        }
        this.prevAngle = this.angle;
        if (!this.onGround && !this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            this.angle += Math.PI * Math.sin(this.rotateFactor * this.age)/2;
        }
        if (this.world.getFluidState(new BlockPos(this.x, this.y, this.z)).isIn(FluidTags.WATER)) {
            this.velocityY = 0;
            this.gravityStrength = 0;
        } else {
            this.gravityStrength = 0.1F;
        }
    }

    public int getColorMultiplier(float tint) {
        return 200;
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider provider;

        public DefaultFactory(SpriteProvider provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GoldenOakLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.provider);
        }
    }
}
