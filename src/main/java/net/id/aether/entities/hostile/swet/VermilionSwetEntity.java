package net.id.aether.entities.hostile.swet;

import net.id.aether.client.rendering.particle.AetherParticles;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

// Not yet implemented, not planned for 1.6.0
public class VermilionSwetEntity extends SwetEntity {
    public VermilionSwetEntity(EntityType<? extends VermilionSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityCollision(Entity entity) {
        if (entity instanceof TntMinecartEntity tnt) {
            if (!tnt.isPrimed() && this.getSize() >= 4) {
                tnt.prime();
            }
        }
        if (isAbsorbable(entity, world)) {
            entity.setOnFireFor(2/*seconds*/);
        }

        super.onEntityCollision(entity);
    }

    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return SwetEntity.canSpawn(type, world, spawnReason, pos, random) &&
                (world.getStatesInBoxIfLoaded(Box.of(Vec3d.of(pos), 4, 2, 4)).anyMatch(state -> state.isIn(AetherBlockTags.SWET_TRANSFORMERS_VERMILION))
                        || world.getRandom().nextFloat() < 0.01);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        // Missing texture magenta
        return AetherParticles.coloredSplash(0xFF_00_FF);
    }
}
