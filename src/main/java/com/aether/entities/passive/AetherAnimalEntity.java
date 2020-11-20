package com.aether.entities.passive;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class AetherAnimalEntity extends AnimalEntity {

    protected AetherAnimalEntity(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
        // TODO: ???
        // this.spawningGround = AetherBlocks.aether_grass;
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
