package com.aether.entities.projectile;

import com.aether.entities.AetherEntityTypes;
import com.aether.items.AetherItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class EnchantedDartEntity extends DartEntity {
    public EnchantedDartEntity(EntityType<? extends EnchantedDartEntity> entityType, World world) {
        super(entityType, world);
        this.setDamage(6);
    }

    public EnchantedDartEntity(double x, double y, double z, World world) {
        super(AetherEntityTypes.ENCHANTED_DART, x, y, z, world);
        this.setDamage(6);
    }

    public EnchantedDartEntity(LivingEntity owner, World world) {
        super(AetherEntityTypes.ENCHANTED_DART, owner, world);
        this.setDamage(6);
    }

    @Override
    protected ItemStack asItemStack() {
        return new ItemStack(AetherItems.ENCHANTED_DART);
    }
}