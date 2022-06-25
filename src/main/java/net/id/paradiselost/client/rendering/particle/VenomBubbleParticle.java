package net.id.paradiselost.client.rendering.particle;


import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.util.math.BlockPos;

import java.util.SplittableRandom;

@Environment(EnvType.CLIENT)
public class VenomBubbleParticle extends SpriteBillboardParticle {

    private static final SplittableRandom random = new SplittableRandom();
    private final SpriteProvider spriteProvider;
    private int ticksUntilTextureChange, nextTexture = 2;
    private final double xMult = random.nextDouble();
    private final double zMult = random.nextDouble();
    private boolean dying;


    protected VenomBubbleParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider provider) {
        super(world, x, y, z, velocityX, velocityY, velocityZ);

        spriteProvider = provider;

        setSprite(spriteProvider.getSprite(0, 4));

        collidesWithWorld = true;
        gravityStrength = 0.07F;
        maxAge = 50 + random.nextInt(50);

        ticksUntilTextureChange = 15 + random.nextInt(6);

        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;

        scale = (float) (0.05 + (random.nextDouble() * 0.05));
    }

    @Override
    public void tick() {
        float lifeStage = getLifeProgress();

        double velocityXWaver = Math.sin(age) / 15 * xMult;
        double velocityZWaver = Math.cos(age) / 15 * zMult;

        if((ticksUntilTextureChange <= 0 && nextTexture > 4)) {
            markDead();
            return;
        }

        if(world.getTime() % 30 == 0 && random.nextBoolean()) {
            velocityY += random.nextDouble(0.001, 0.025);
        }

        if(ticksUntilTextureChange <= 0) {
            if(!dying) {
                ticksUntilTextureChange = 15 + random.nextInt(6);
                setSprite(spriteProvider.getSprite(nextTexture, 4));
                nextTexture = nextTexture == 2 ? 1 : 2;
            }
            else {
                if(nextTexture < 2) {
                    nextTexture = 2;
                }

                setSprite(spriteProvider.getSprite(nextTexture, 4));
                ticksUntilTextureChange = 10 / nextTexture;
                nextTexture++;
            }
        }

        if(!world.getFluidState(new BlockPos(x, y, z)).isEmpty()) {
            updatePos();
            move(velocityX + velocityXWaver, velocityY / 2.5, velocityZ + velocityZWaver);
            age += random.nextInt(3);
        }
        else {
            updatePos();
            move(velocityX + velocityXWaver, velocityY / 2, velocityZ + velocityZWaver);
            age++;
        }
        ticksUntilTextureChange--;

        if(age > maxAge) {
            dying = true;
        }
    }

    public float getLifeProgress() {
        return (float) age / maxAge;
    }

    public void updatePos() {
        prevPosX = x;
        prevPosY = y;
        prevPosZ = z;
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
    public static class DefaultFactory implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider provider;

        public DefaultFactory(SpriteProvider provider) {
            this.provider = provider;
        }

        @Override
        public Particle createParticle(DefaultParticleType parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
            return new VenomBubbleParticle(world, x, y, z, velocityX, velocityY, velocityZ, provider);
        }
    }
}
