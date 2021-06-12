package com.aether.items.weapons;

import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonDartEntity;
import com.aether.items.AetherItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Dart extends Item {
    public Dart(Properties settings) {
        super(settings);
    }

    public AbstractArrow createDart(Level world, ItemStack stack, LivingEntity entity) {
        if (this == AetherItems.ENCHANTED_DART) return new EnchantedDartEntity(entity, world);
        else if (this == AetherItems.POISON_DART) return new PoisonDartEntity(entity, world);
        return new GoldenDartEntity(entity, world);
    }
}