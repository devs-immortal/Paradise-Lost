package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GoldenDartEntity extends DartEntity {

    public GoldenDartEntity(double x, double y, double z, World world) {
        super(AetherEntityTypes.GOLDEN_DART, x, y, z, world);
        this.setDamage(4);
    }

    public GoldenDartEntity(LivingEntity owner, World world) {
        super(AetherEntityTypes.GOLDEN_DART, owner, world);
        this.setDamage(4);
    }

    public GoldenDartEntity(World world) {
        super(AetherEntityTypes.GOLDEN_DART, world);
        this.setDamage(4);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(AetherItems.GOLDEN_DART);
    }
}