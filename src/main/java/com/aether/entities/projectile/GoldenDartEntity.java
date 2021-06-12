package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class GoldenDartEntity extends DartEntity {

    public GoldenDartEntity(double x, double y, double z, Level world) {
        super(AetherEntityTypes.GOLDEN_DART, x, y, z, world);
        this.setBaseDamage(4);
    }

    public GoldenDartEntity(LivingEntity owner, Level world) {
        super(AetherEntityTypes.GOLDEN_DART, owner, world);
        this.setBaseDamage(4);
    }

    public GoldenDartEntity(Level world) {
        super(AetherEntityTypes.GOLDEN_DART, world);
        this.setBaseDamage(4);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(AetherItems.GOLDEN_DART);
    }
}