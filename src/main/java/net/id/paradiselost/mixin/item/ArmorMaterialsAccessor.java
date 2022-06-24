package net.id.paradiselost.mixin.item;

import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(ArmorMaterials.class)
public interface ArmorMaterialsAccessor {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    static ArmorMaterials callInit(String valueName, int ordinal, String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        throw new AssertionError();
    }
}
