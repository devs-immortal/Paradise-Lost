package net.id.aether.entities.hostile.swet;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class BlueSwetEntity extends SwetEntity {
    public BlueSwetEntity(EntityType<? extends BlueSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public void tick() {
        if (this.world.getBiome(this.getBlockPos()).value().getTemperature() <= 0.2F) {
            this.setInPowderSnow(true);
        }

        super.tick();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            int i = MathHelper.floor(this.getX());
            int j = MathHelper.floor(this.getY());
            int k = MathHelper.floor(this.getZ());
            BlockPos blockPos = new BlockPos(i, j, k);
            if (this.world.getBiome(blockPos).value().getTemperature() > 1.0f) {
                this.damage(DamageSource.ON_FIRE, 1.0f);
            }
        }
    }

    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return SwetEntity.canSpawn(type, world, spawnReason, pos, random) &&
                (world.getStatesInBoxIfLoaded(Box.of(Vec3d.of(pos), 4, 2, 4)).anyMatch(state -> state.isIn(AetherBlockTags.SWET_TRANSFORMERS_BLUE))
                        || world.getRandom().nextFloat() < 0.01);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        return AetherParticles.coloredSplash(0x52_7A_8E);
    }
}
