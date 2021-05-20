package com.aether.items.weapons;

import com.aether.entities.projectile.EnchantedDartEntity;
import com.aether.entities.projectile.GoldenDartEntity;
import com.aether.entities.projectile.PoisonDartEntity;
import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class Dart extends Item {
    public Dart(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createDart(World world, ItemStack stack, LivingEntity entity) {
        if (this == AetherItems.ENCHANTED_DART) return new EnchantedDartEntity(entity, world);
        else if (this == AetherItems.POISON_DART) return new PoisonDartEntity(entity, world);
        return new GoldenDartEntity(entity, world);
    }
}