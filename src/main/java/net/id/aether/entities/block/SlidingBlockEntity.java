package net.id.aether.entities.block;

import net.minecraft.entity.EntityType;
import net.minecraft.world.World;

public class SlidingBlockEntity extends BlockLikeEntity {
    public SlidingBlockEntity(EntityType<?> type, World world) {
        super(type, world);
    }
}
