package net.id.aether.entities.projectile;

import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PoisonNeedleEntity extends PoisonDartEntity {
    public PoisonNeedleEntity(EntityType<? extends PoisonDartEntity> entityType, World world) {
        super(entityType, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(double x, double y, double z, World world) {
        super(AetherEntityTypes.POISON_NEEDLE, x, y, z, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(LivingEntity owner, World world) {
        super(AetherEntityTypes.POISON_NEEDLE, owner, world);
        this.setNoGravity(false);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
