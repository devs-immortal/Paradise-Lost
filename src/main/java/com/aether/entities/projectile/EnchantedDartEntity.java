package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class EnchantedDartEntity extends DartEntity {

    public EnchantedDartEntity(double x, double y, double z, Level world) {
        super(AetherEntityTypes.ENCHANTED_DART, x, y, z, world);
        this.setBaseDamage(6);
    }

    public EnchantedDartEntity(LivingEntity owner, Level world) {
        super(AetherEntityTypes.ENCHANTED_DART, owner, world);
        this.setBaseDamage(6);
    }

    public EnchantedDartEntity(Level world) {
        super(AetherEntityTypes.ENCHANTED_DART, world);
        this.setBaseDamage(6);
    }

    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(AetherItems.ENCHANTED_DART);
    }
}