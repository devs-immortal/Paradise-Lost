package com.aether.items.weapons;

import com.aether.items.AetherItemGroups;
import com.aether.items.AetherItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Rarity;
import net.minecraft.world.World;

public class Dart extends Item {

    public Dart(Rarity rarity) {
        super(new Settings().rarity(rarity).group(AetherItemGroups.Weapons));
    }

    public ProjectileEntity createDart(World world, ItemStack stack, LivingEntity entity) {
        if (this == AetherItems.enchanted_dart) return null;
        else if (this == AetherItems.poison_dart) return null;

        //TODO: Implement EntityEnchantedDart, EntityPoisonDart and EntityGoldenDart

        return null;
    }
}