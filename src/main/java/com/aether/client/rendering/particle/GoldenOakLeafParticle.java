package com.aether.client.rendering.particle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;

import java.util.SplittableRandom;

//I did not steal this from that one falling leaves mod, you have no proof!
public class GoldenOakLeafParticle extends TextureSheetParticle {

    private static final SplittableRandom random = new SplittableRandom();
    private final float rotateFactor;
    private final double velocityComposite, velocityDown;

    protected GoldenOakLeafParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet provider) {
        super(clientWorld, d, e, f);
        this.pickSprite(provider);

        this.hasPhysics = true;
        this.gravity = 0.1F;
        this.lifetime = 400;

        this.xd *= 0.3F;
        this.yd *= 0.0F;
        this.zd *= 0.3F;

        this.velocityComposite = g / 50;
        velocityDown = h;

        this.rotateFactor = ((float) Math.random() - 0.5F) * 0.01F;
        this.quadSize = (float) (0.08 + (random.nextDouble() / 10));
    }

    public void tick() {
        yd = velocityDown;
        super.tick();
        this.zd = velocityComposite / 2;
        this.xd = velocityComposite / 2;
        if (this.age < 2) {
            this.yd = 0;
        }
        if (this.age > this.lifetime - 1 / 0.06F) {
            if (this.alpha > 0.06F) {
                this.alpha -= 0.06F;
            } else {
                this.remove();
            }
        }
        this.oRoll = this.roll;
        if (!this.onGround && !this.level.getFluidState(new BlockPos(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.roll += Math.PI * Math.sin(this.rotateFactor * this.age) / 2;
        }
        if (this.level.getFluidState(new BlockPos(this.x, this.y, this.z)).is(FluidTags.WATER)) {
            this.yd = 0;
            this.gravity = 0;
        } else {
            this.gravity = 0.1F;
        }
    }

    public int getLightColor(float tint) {
        return 200;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new GoldenOakLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.provider);
        }
    }
}
