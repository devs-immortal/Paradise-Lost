package net.id.aether.entities.hostile.swet;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.TntMinecartEntity;
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
        super.onEntityCollision(entity);
    }
}
