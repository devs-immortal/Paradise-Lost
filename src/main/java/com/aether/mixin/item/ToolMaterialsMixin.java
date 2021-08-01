package com.aether.mixin.item;

import com.aether.util.EnumExtender;
import net.minecraft.item.ToolMaterials;
import net.minecraft.recipe.Ingredient;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
@Mixin(ToolMaterials.class)
public class ToolMaterialsMixin {
    @SuppressWarnings("ShadowTarget")
    @Shadow
    @Mutable
    @Final
    private static ToolMaterials[] field_8926;

    private ToolMaterialsMixin(String valueName, int ordinal, int miningLevel, int itemDurability, float miningSpeed, float attackDamage, int enchantability, Supplier<Ingredient> repairIngredient) {
        throw new AssertionError();
    }

    static {
        EnumExtender.register(ToolMaterials.class, (name, args) -> {
            ToolMaterials entry = (ToolMaterials) (Object) new ToolMaterialsMixin(name, field_8926.length,
                    (int) args[0], (int) args[1], (float) args[2], (float) args[3], (int) args[4], (Supplier<Ingredient>) args[5]);

            field_8926 = Arrays.copyOf(field_8926, field_8926.length + 1);
            return field_8926[field_8926.length - 1] = entry;
        });
    }
}
