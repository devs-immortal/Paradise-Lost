package net.id.paradiselost.items.weapons;

import net.id.paradiselost.entities.projectile.EnchantedDartEntity;
import net.id.paradiselost.entities.projectile.GoldenDartEntity;
import net.id.paradiselost.entities.projectile.PoisonDartEntity;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DartItem extends Item {
    public DartItem(Settings settings) {
        super(settings);
    }

    public PersistentProjectileEntity createDart(World world, ItemStack stack, LivingEntity entity) {
        if (this == ParadiseLostItems.ENCHANTED_DART) {
            return new EnchantedDartEntity(entity, world);
        } else if (this == ParadiseLostItems.POISON_DART) {
            return new PoisonDartEntity(entity, world);
        }
        return new GoldenDartEntity(entity, world);
    }
}
