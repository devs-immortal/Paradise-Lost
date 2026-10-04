package net.id.paradise_lost.client.rendering.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class LitCloudParticle extends TextureSheetParticle {
    private final SpriteSet spriteProvider;

    LitCloudParticle(ClientLevel world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteSet spriteProvider) {
        super(world, x, y, z, 0.0, 0.0, 0.0);
        this.friction = 0.96F;
        this.spriteProvider = spriteProvider;
        float f = 2.5F;
        this.xd *= 0.10000000149011612;
        this.yd *= 0.10000000149011612;
        this.zd *= 0.10000000149011612;
        this.xd += velocityX;
        this.yd += velocityY;
        this.zd += velocityZ;
        float g = 1.0F - (float) (Math.random() * 0.30000001192092896);
        this.rCol = g;
        this.gCol = g;
        this.bCol = g;
        this.quadSize *= 1.875F;
        int i = (int) (8.0 / (Math.random() * 0.8 + 0.3));
        this.lifetime = (int) Math.max((float) i * 2.5F, 1.0F);
        this.hasPhysics = false;
        this.setSpriteFromAge(spriteProvider);
    }

    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    public float getQuadSize(float tickDelta) {
        return this.quadSize * Mth.clamp(((float) this.age + tickDelta) / (float) this.lifetime * 32.0F, 0.0F, 1.0F);
    }

    public void tick() {
        super.tick();
        if (!this.removed) {
            this.setSpriteFromAge(this.spriteProvider);
            Player playerEntity = this.level.getNearestPlayer(this.x, this.y, this.z, 2.0, false);
            if (playerEntity != null) {
                double d = playerEntity.getY();
                if (this.y > d) {
                    this.y += (d - this.y) * 0.2;
                    this.yd += (playerEntity.getDeltaMovement().y - this.yd) * 0.2;
                    this.setPos(this.x, this.y, this.z);
                }
            }
        }

    }

    @Override
    public int getLightColor(float tint) {
        return 240;
    }

    public static class DefaultFactory implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteProvider;

        public DefaultFactory(SpriteSet spriteProvider) {
            this.spriteProvider = spriteProvider;
        }

        public Particle createParticle(SimpleParticleType simpleParticleType, ClientLevel clientWorld, double d, double e, double f, double g, double h, double i) {
            return new LitCloudParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
        }
    }
}
