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

    protected AetherAnimalEntity(World world) {
        // TODO: If anyone knows why this has to be here and what do to with it, you are more than welcome to change it
        super(EntityType.COW, world);
    }

    protected void initAttributes() {
        //
    }

    @Override
    public @Nullable PassiveEntity createChild(ServerWorld world, PassiveEntity entity) {
        return null;
    }
}
