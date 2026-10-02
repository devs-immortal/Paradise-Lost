package net.id.paradiselost.items.food;

import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;

@SuppressWarnings("unused")
public class ParadiseLostFoodComponent {
    public static final FoodComponent BLACKCURRANT = new FoodComponent.Builder().nutrition(2).saturationModifier(0.5F).build();
    public static final ConsumableComponent BLACKCURRANT_CONSUMABLE = ConsumableComponents.food().consumeSeconds(0.8F).build();
    public static final FoodComponent MOA_MEAT = new FoodComponent.Builder().nutrition(3).saturationModifier(0.3F).build();
    public static final FoodComponent COOKED_MOA_MEAT = new FoodComponent.Builder().nutrition(6).saturationModifier(1F).build();
    public static final FoodComponent POPOM_JELLY = new FoodComponent.Builder().nutrition(1).saturationModifier(1.0F).alwaysEdible().build();
    public static final ConsumableComponent POPOM_JELLY_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0), 1.0F)).build();
    public static final FoodComponent SWEDROOT = new FoodComponent.Builder().nutrition(2).saturationModifier(1.5F).build();
    public static final FoodComponent GENERIC_WORSE = new FoodComponent.Builder().nutrition(1).saturationModifier(0.25F).build();
    public static final ConsumableComponent GENERIC_WORSE_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0), 0.075F)).build();
    public static final FoodComponent AMADRYS_NOODLES = new FoodComponent.Builder().nutrition(7).saturationModifier(0.5F).build();
    public static final FoodComponent AMADRYS_BREAD = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
    public static final FoodComponent AMADRYS_BREAD_GLAZED = new FoodComponent.Builder().nutrition(8).saturationModifier(1.4F).build();
    public static final FoodComponent AMADRYS_BREAD_GLAZED_FILLED = new FoodComponent.Builder().nutrition(9).saturationModifier(1.4F).build();
    public static final ConsumableComponent AMADRYS_BREAD_GLAZED_FILLED_CONSUMABLE = ConsumableComponents.food()
            .consumeEffect(new ApplyEffectsConsumeEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 0), 1.0F)).build();
    public static final FoodComponent BLACKCURRANT_PIE = new FoodComponent.Builder().nutrition(9).saturationModifier(0.2F).build();
    public static final FoodComponent BLACKCURRANT_COOKIE = new FoodComponent.Builder().nutrition(5).saturationModifier(1.2F).build();
    public static final FoodComponent ROOT_STEW = new FoodComponent.Builder().nutrition(10).saturationModifier(1.5F).build();
}
