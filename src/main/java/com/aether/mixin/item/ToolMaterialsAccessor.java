package com.aether.mixin.item;

import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(ToolMaterials.class)
public interface ToolMaterialsAccessor {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    static ToolMaterials callInit(String valueName, int ordinal, int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        throw new AssertionError();
    }
}
