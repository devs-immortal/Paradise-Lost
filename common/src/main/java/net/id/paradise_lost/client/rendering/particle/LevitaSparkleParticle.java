package net.id.paradise_lost.client.rendering.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.NotNull;

import java.util.SplittableRandom;

public class LevitaSparkleParticle extends TextureSheetParticle {

    private static final SplittableRandom random = new SplittableRandom();
    private final SpriteSet sprites;
    private final float polarity;

    protected LevitaSparkleParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.lifetime = 12;
        this.gravity = -random.nextFloat(0.002F, 0.004F);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.polarity = random.nextBoolean() ? 0.01F : -0.01F;
        //this.roll = random.nextFloat();
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.yd = this.yd - (double)this.gravity;
            this.roll += polarity;
            this.move(this.xd, this.yd, this.zd);
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public int getLightColor(float tint) {
        return 240;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public record DefaultFactory(SpriteSet spriteProvider) implements ParticleProvider<SimpleParticleType> {

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
                return new LevitaSparkleParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
            }
        }

}
