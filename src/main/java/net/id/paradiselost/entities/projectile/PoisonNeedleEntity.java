package net.id.paradiselost.entities.projectile;

import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PoisonNeedleEntity extends PoisonDartEntity {
    public PoisonNeedleEntity(EntityType<? extends PoisonDartEntity> entityType, World world) {
        super(entityType, world);
        setNoGravity(false);
    }

    public PoisonNeedleEntity(double x, double y, double z, World world) {
        super(ParadiseLostEntityTypes.POISON_NEEDLE, x, y, z, world);
        setNoGravity(false);
    }

    public PoisonNeedleEntity(LivingEntity owner, World world) {
        super(ParadiseLostEntityTypes.POISON_NEEDLE, owner, world);
        setNoGravity(false);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}
