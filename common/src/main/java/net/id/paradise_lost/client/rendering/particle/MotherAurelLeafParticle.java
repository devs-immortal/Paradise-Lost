package net.id.paradise_lost.client.rendering.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.tags.FluidTags;
import java.util.SplittableRandom;

public class MotherAurelLeafParticle extends TextureSheetParticle {

    private static final SplittableRandom random = new SplittableRandom();
    private final float rotateFactor;
    private final double velocityComposite, velocityDown;

    protected MotherAurelLeafParticle(ClientLevel clientWorld, double d, double e, double f, double g, double h, double i, SpriteSet provider) {
        super(clientWorld, d, e, f);
        this.pickSprite(provider);

        this.hasPhysics = true;
        this.gravity = 0.09F;
        this.lifetime = 1200;

        this.xd *= 0.325F;
        this.yd *= 0.0F;
        this.zd *= 0.325F;

        this.velocityComposite = g / 50;
        velocityDown = h;

        this.rotateFactor = ((float) Math.random() - 0.5F) * 0.002F;
        this.quadSize = (float) (0.06 + (random.nextDouble() / 14));
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
        if (!this.onGround && !this.level.getFluidState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).is(FluidTags.WATER)) {
            this.roll += (float) (Math.PI * Math.sin(this.rotateFactor * this.age) / 2);
        }
        if (this.level.getFluidState(new BlockPos((int) this.x, (int) this.y, (int) this.z)).is(FluidTags.WATER)) {
            this.yd = 0;
            this.gravity = 0;
        } else {
            this.gravity = 0.1F;
        }
    }

    @Override
    public int getLightColor(float tint) {
        return 200;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet provider;

        public DefaultFactory(SpriteSet provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(SimpleParticleType parameters, ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new MotherAurelLeafParticle(world, x, y, z, velocityX, velocityY, velocityZ, this.provider);
        }
    }
}
