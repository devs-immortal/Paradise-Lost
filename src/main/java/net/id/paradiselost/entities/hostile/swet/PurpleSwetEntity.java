package net.id.paradiselost.entities.hostile.swet;

import net.id.incubus_core.condition.api.ConditionAPI;
import net.id.incubus_core.condition.api.Persistence;
import net.id.incubus_core.condition.base.ConditionManager;
import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.id.paradiselost.effect.condition.Conditions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;

import static net.id.paradiselost.tag.ParadiseLostBlockTags.SWET_TRANSFORMERS_PURPLE;

public class PurpleSwetEntity extends SwetEntity {
    public PurpleSwetEntity(EntityType<? extends PurpleSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void onEntityCollision(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ConditionManager manager = ConditionAPI.getConditionManager(livingEntity);
            manager.add(Conditions.VENOM, Persistence.TEMPORARY, 1.5F);
        }
        super.onEntityCollision(entity);
    }
    
    public static boolean canSpawn(EntityType<? extends SwetEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return SwetEntity.canSpawn(type, world, spawnReason, pos, random) &&
               (world.getStatesInBoxIfLoaded(Box.of(Vec3d.of(pos), 4, 2, 4)).anyMatch(state -> state.isIn(SWET_TRANSFORMERS_PURPLE))
                || world.getRandom().nextFloat() < 0.02);
    }
    
    @Override
    protected ParticleEffect createParticle() {
        return ParadiseLostParticles.coloredSplash(0x71_52_8E);
    }
}
