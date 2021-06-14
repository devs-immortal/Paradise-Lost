package com.aether.items.utils;

import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.util.Lazy;

public class AetherItemTier implements ToolMaterial {
    private final int harvestLevel;
    private final int maxUses;
    private final float damageVsEntity;
    private final int enchantability;
    private final float speedMultiplier;
    private final Lazy<Ingredient> ingredientLoader;

    public AetherItemTier(int harvestLevel, int maxUses, float speedMultiplier, float damageVsEntity, int enchantability, ItemConvertible repairMaterial) {
        this.harvestLevel = harvestLevel;
        this.maxUses = maxUses;
        this.speedMultiplier = speedMultiplier;
        this.damageVsEntity = damageVsEntity;
        this.enchantability = enchantability;
        this.ingredientLoader = new Lazy<>(() -> Ingredient.ofItems(repairMaterial));
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.ingredientLoader.get();
    }

    @Override
    public int getMiningLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getDurability() {
        return this.maxUses;
    }

    @Override
    public int getEnchantability() {
        return this.enchantability;
    }

    @Override
    public float getMiningSpeedMultiplier() {
        return this.speedMultiplier;
    }

    @Override
    public float getAttackDamage() {
        return this.damageVsEntity;
    }
}