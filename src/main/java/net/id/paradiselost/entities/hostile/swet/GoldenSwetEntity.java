package net.id.paradiselost.entities.hostile.swet;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

public class GoldenSwetEntity extends SwetEntity {
    public GoldenSwetEntity(EntityType<? extends GoldenSwetEntity> entityType, World world) {
        super(entityType, world);
        this.initialSize = 4;
    }

    public static DefaultAttributeContainer.Builder createSwetAttributes() {
        return HostileEntity.createHostileAttributes()
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 8.0)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 40);
    }

    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return SwetEntity.canSpawn(type, world, spawnReason, pos, random)
                && (world.getStatesInBoxIfLoaded(Box.of(Vec3d.of(pos), 4, 2, 4)).anyMatch(state -> state.isIn(ParadiseLostBlockTags.SWET_TRANSFORMERS_GOLDEN))
                        || world.getRandom().nextFloat() < 0.03);
    }

    @Override
    protected ParticleEffect createParticle() {
        return ParadiseLostParticles.coloredSplash(0xAC_9E_35);
    }
}
