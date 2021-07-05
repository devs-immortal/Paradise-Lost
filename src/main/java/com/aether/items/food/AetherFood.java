package com.aether.items.food;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.FoodComponent;

public class AetherFood {
    public static final FoodComponent GENERIC;
    public static final FoodComponent GUMMY_SWET;
    public static final FoodComponent BLUEBERRY, ENCHANTED_BLUEBERRY, ORANGE;
    public static final FoodComponent WHITE_APPLE;
    public static final FoodComponent HEALING_STONE;
    public static final FoodComponent MILK;

    static {
        GENERIC = new FoodComponent.Builder().hunger(2).saturationModifier(1.5F).build();
        GUMMY_SWET = new FoodComponent.Builder().hunger(8).saturationModifier(0.5F).build();
        BLUEBERRY = new FoodComponent.Builder().hunger(2).saturationModifier(0.5F).snack().build();
        ENCHANTED_BLUEBERRY = new FoodComponent.Builder().hunger(8).saturationModifier(1.0F).snack().build();
        ORANGE = new FoodComponent.Builder().hunger(5).saturationModifier(0.8F).snack().build();
        WHITE_APPLE = new FoodComponent.Builder().saturationModifier(5.0F).alwaysEdible().build();
        HEALING_STONE = new FoodComponent.Builder().saturationModifier(2.5F).alwaysEdible().snack().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 610, 0), 1.0F).build();
        MILK = new FoodComponent.Builder().hunger(12).saturationModifier(2F).snack().alwaysEdible().statusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 1200, 2), 1F).statusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 3600, 4), 1F).statusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 3600, 1), 1F).build();
    }
}