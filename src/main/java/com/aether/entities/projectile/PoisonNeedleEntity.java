package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PoisonNeedleEntity extends PoisonDartEntity {

    public PoisonNeedleEntity(double x, double y, double z, World world) {
        super(AetherEntityTypes.POISON_NEEDLE, x, y, z, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(LivingEntity owner, World world) {
        super(AetherEntityTypes.POISON_NEEDLE, owner, world);
        this.setNoGravity(false);
    }

    public PoisonNeedleEntity(World world) {
        super(AetherEntityTypes.POISON_NEEDLE, world);
        this.setNoGravity(false);
    }

    @Override
    protected ItemStack asItemStack() {
        return ItemStack.EMPTY;
    }
}