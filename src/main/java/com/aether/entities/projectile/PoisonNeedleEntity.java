package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PoisonNeedleEntity extends PoisonDartEntity {

    public PoisonNeedleEntity(double x, double y, double z, Level world) {
        super(AetherEntityTypes.POISON_NEEDLE, x, y, z, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(LivingEntity owner, Level world) {
        super(AetherEntityTypes.POISON_NEEDLE, owner, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(Level world) {
        super(AetherEntityTypes.POISON_NEEDLE, world);
        this.setNoGravity(false);
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }
}