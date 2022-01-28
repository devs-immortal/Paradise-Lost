package net.id.aether.client.rendering.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.Aether;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ColoredSplashParticle extends RainSplashParticle {
    ColoredSplashParticle(ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
        super(clientWorld, x, y, z);
        this.gravityStrength = 0.04f;
        if (h == 0.0 && (g != 0.0 || i != 0.0)) {
            this.velocityX = g;
            this.velocityY = 0.1;
            this.velocityZ = i;
        }
    }

    @Override
    public ParticleTextureSheet getType() {
        return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(value=EnvType.CLIENT)
    public static class SplashFactory
            implements ParticleFactory<DefaultParticleType> {
        private final SpriteProvider spriteProvider;
        private float red = 0.0F;
        private float green = 0.0F;
        private float blue = 0.0F;
        private float alpha = 1.0F;

        private boolean usingHex = false;
        private int hexCode = 0xFFFFFF;

        public SplashFactory(SpriteProvider spriteProvider, float red, float green, float blue, float alpha) {
            this.spriteProvider = spriteProvider;

            this.red = red;
            this.green = green;
            this.blue = blue;
            this.alpha = alpha;
        }

        public SplashFactory(SpriteProvider spriteProvider, float red, float green, float blue) {
            this(spriteProvider, red, green, blue, 1.0F);
        }

        public SplashFactory(SpriteProvider spriteProvider) {
            this(spriteProvider, 1.0F, 1.0F, 1.0F);
        }

        public SplashFactory(SpriteProvider spriteProvider, int hex) {
            this.spriteProvider = spriteProvider;

            this.usingHex = true;
            this.hexCode = hex;
        }

        public void setColor(int rgbHex) {
            float f = (float)((rgbHex & 0xFF0000) >> 16) / 255.0f;
            float g = (float)((rgbHex & 0xFF00) >> 8) / 255.0f;
            float h = (float)((rgbHex & 0xFF) >> 0) / 255.0f;
            float i = 1.0f;
            //this.setColor(f * 1.0f, g * 1.0f, h * 1.0f);
            //this.setAlpha(i);
            this.red = f * 1.0f;
            this.green = g * 1.0f;
            this.blue = h * 1.0f;
            this.alpha = i;
        }

        public static float randomNumberGenerator()
        {
            float rangeMin = 0.0f;
            float rangeMax = 1.0f;
            Random r = new Random();
            float createdRanNum = rangeMin + (rangeMax - rangeMin) * r.nextFloat();
            return createdRanNum;
        }

        @Override
        public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double g, double h, double i) {
            ColoredSplashParticle splashParticle = new ColoredSplashParticle(clientWorld, x, y, z, g, h, i);
            splashParticle.setSprite(this.spriteProvider);
            if (this.usingHex) {
                setColor(this.hexCode);
            }
//            splashParticle.setColor(this.red, this.green, this.blue);
            splashParticle.setColor(randomNumberGenerator(), randomNumberGenerator(), randomNumberGenerator());
            splashParticle.setAlpha(this.alpha);
            Aether.LOG.info("Color {} {} {} {} ({})", red, green, blue, alpha, hexCode);
            return splashParticle;
        }
    }
}
