package com.aether.mixin.item;

import com.aether.util.EnumExtender;
import net.minecraft.item.ArmorMaterials;
import net.minecraft.recipe.Ingredient;
import net.minecraft.sound.SoundEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
@Mixin(ArmorMaterials.class)
public class ArmorMaterialsMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Mutable
    @Final
    private static ArmorMaterials[] field_7888;

    private ArmorMaterialsMixin(String valueName, int ordinal, String name, int durabilityMultiplier, int[] protectionAmounts, int enchantability, SoundEvent equipSound, float toughness, float knockbackResistance, Supplier<Ingredient> repairIngredientSupplier) {
        throw new AssertionError();
    }

    static {
        EnumExtender.register(ArmorMaterials.class, (name, args) -> {
            ArmorMaterials entry = (ArmorMaterials) (Object) new ArmorMaterialsMixin(name, field_7888.length,
                    (String) args[0], (int) args[1], (int[]) args[2], (int) args[3], (SoundEvent) args[4], (float) args[5], (float) args[6], (Supplier<Ingredient>) args[7]);

            field_7888 = Arrays.copyOf(field_7888, field_7888.length + 1);
            return field_7888[field_7888.length - 1] = entry;
        });
    }
}
