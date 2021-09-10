package net.id.aether.items.food;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

@SuppressWarnings("unused")
public class AetherFoodComponent {
    public static final FoodComponent BLUEBERRY = new FoodComponent.Builder().hunger(2).saturationModifier(0.5F).snack().build();
    public static final FoodComponent ENCHANTED_BLUEBERRY = new FoodComponent.Builder().hunger(8).saturationModifier(1.0F).snack().build();
    public static final FoodComponent ORANGE = new FoodComponent.Builder().hunger(5).saturationModifier(0.8F).snack().build();
    public static final FoodComponent MOA_MEAT = new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build();
    public static final FoodComponent COOKED_MOA_MEAT = new FoodComponent.Builder().hunger(6).saturationModifier(1F).meat().build();
    public static final FoodComponent GUMMY_SWET = new FoodComponent.Builder().hunger(8).saturationModifier(0.5F).build();
    public static final FoodComponent WHITE_APPLE = new FoodComponent.Builder().alwaysEdible().build();
    public static final FoodComponent HEALING_STONE = new FoodComponent.Builder().saturationModifier(2.5F).alwaysEdible().snack()
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 610, 0), 1.0F).build();
    public static final FoodComponent GENERIC = new FoodComponent.Builder().hunger(2).saturationModifier(1.5F).build();
    public static final FoodComponent GENERIC_WORSE = new FoodComponent.Builder().hunger(1).saturationModifier(0.25F)
            .statusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 0), 0.075F).build();
    public static final FoodComponent VALKYRIE_MILK = new FoodComponent.Builder().hunger(12).saturationModifier(2F).snack().alwaysEdible()
            .statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 2), 1F)
            .statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 4), 1F)
            .statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 1), 1F).build();
}
