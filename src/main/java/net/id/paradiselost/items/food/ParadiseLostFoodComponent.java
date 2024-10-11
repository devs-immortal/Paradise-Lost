package net.id.paradiselost.items.food;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;

@SuppressWarnings("unused")
public class ParadiseLostFoodComponent {
    public static final FoodComponent BLACKCURRANT = new FoodComponent.Builder().nutrition(2).saturationModifier(0.5F).snack().build();
    public static final FoodComponent ORANGE = new FoodComponent.Builder().nutrition(5).saturationModifier(0.8F).snack().build();
    public static final FoodComponent MOA_MEAT = new FoodComponent.Builder().nutrition(3).saturationModifier(0.3F).build();
    public static final FoodComponent COOKED_MOA_MEAT = new FoodComponent.Builder().nutrition(6).saturationModifier(1F).build();
    public static final FoodComponent SWEDROOT = new FoodComponent.Builder().nutrition(2).saturationModifier(1.5F).build();
    public static final FoodComponent GENERIC_WORSE = new FoodComponent.Builder().nutrition(1).saturationModifier(0.25F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0), 0.075F).build();
    public static final FoodComponent AMADRYS_NOODLES = new FoodComponent.Builder().nutrition(7).saturationModifier(0.5F).usingConvertsTo(Items.BOWL).build();
    public static final FoodComponent AMADRYS_BREAD = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
    public static final FoodComponent AMADRYS_BREAD_GLAZED = new FoodComponent.Builder().nutrition(8).saturationModifier(1.4F).build();
}
