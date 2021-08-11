package com.aether.entities.hostile;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class BlueSwetEntity extends SwetEntity {
    public BlueSwetEntity(EntityType<? extends BlueSwetEntity> entityType, World world) {
        super(entityType, world);
        this.setSize(2, true);
    }
}
