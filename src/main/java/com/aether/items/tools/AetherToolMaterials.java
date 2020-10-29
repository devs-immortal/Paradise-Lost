package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.Items;
import net.minecraft.item.ToolMaterial;
import net.minecraft.recipe.Ingredient;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.Lazy;

import java.util.function.Supplier;

public enum AetherToolMaterials implements ToolMaterial {
    SKYROOT(0, 74, 2.0F, 0.0F, 20, () -> {
        return Ingredient.ofItems(AetherBlocks.SKYROOT_PLANK.asItem());
    }),
    HOLYSTONE(1, 131, 4.0F, 1.0F, 5, () -> {
        return Ingredient.ofItems(AetherBlocks.HOLYSTONE.asItem());
    }),
    ZANITE(2, 300, 6.0F, 2.0F, 17, () -> {
        return Ingredient.ofItems(AetherItems.ZANITE_GEM);
    }),
    GRAVITITE(3, 1561, 9.0F, 3.5F, 9, () -> {
        return Ingredient.ofItems(AetherItems.GRAVITITE_INGOT);
    });

    private final int miningLevel;
    private final int itemDurability;
    private final float miningSpeed;
    private final float attackDamage;
    private final int enchantability;
    private final Lazy<Ingredient> repairIngredient;

    private AetherToolMaterials(int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        this.miningLevel = miningLevel;
        this.itemDurability = itemDurability;
        this.miningSpeed = miningSpeed;
        this.attackDamage = attackDamage;
        this.enchantability = enchantability;
        this.repairIngredient = new Lazy(repairIngredient);
    }

    public int getDurability() {
        return this.itemDurability;
    }

    public float getMiningSpeedMultiplier() {
        return this.miningSpeed;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getMiningLevel() {
        return this.miningLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.repairIngredient.get();
    }
}
