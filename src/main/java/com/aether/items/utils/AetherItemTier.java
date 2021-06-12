package com.aether.items.utils;

import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class AetherItemTier implements Tier {
    private final int harvestLevel;
    private final int maxUses;
    private final float damageVsEntity;
    private final int enchantability;
    private final float speedMultiplier;
    private final LazyLoadedValue<Ingredient> ingredientLoader;

    public AetherItemTier(int harvestLevel, int maxUses, float speedMultiplier, float damageVsEntity, int enchantability, ItemLike repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.speedMultiplier = speedMultiplier;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
        this.ingredientLoader = new LazyLoadedValue<>(() -> Ingredient.of(repairMaterial));
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.ingredientLoader.get();
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public float getSpeed() {
        return this.speedMultiplier;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.damageVsEntity;
    }
}