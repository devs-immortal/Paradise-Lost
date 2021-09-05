package net.id.aether.entities.hostile.swet;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.world.World;

public class GoldenSwetEntity extends SwetEntity {
    public GoldenSwetEntity(EntityType<? extends GoldenSwetEntity> entityType, World world) {
        super(entityType, world);
        this.initialSize = 4;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void init() {
        super.init();
        this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(40);
        this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(2);
        this.setHealth(this.getMaxHealth());
    }
}
