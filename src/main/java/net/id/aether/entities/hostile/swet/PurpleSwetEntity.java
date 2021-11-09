package net.id.aether.entities.hostile.swet;

import net.id.aether.api.ConditionAPI;
import net.id.aether.component.ConditionManager;
import net.id.aether.effect.condition.Conditions;
import net.id.aether.effect.condition.Persistence;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;

public class PurpleSwetEntity extends SwetEntity {
    public PurpleSwetEntity(EntityType<? extends PurpleSwetEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void onEntityCollision(Entity entity) {
        if (entity instanceof LivingEntity livingEntity) {
            ConditionManager manager = ConditionAPI.getConditionManager(livingEntity);
            manager.add(Conditions.VENOM, Persistence.CHRONIC, 3F);
        }
        super.onEntityCollision(entity);
    }
}
