package com.aether.items.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;

public class AetherFood {
    public static final FoodProperties DEFAULT;
    public static final FoodProperties ENCHANTED_BLUEBERRY;
    public static final FoodProperties WHITE_APPLE;
    public static final FoodProperties HEALING_STONE;
    public static final FoodProperties MILK;

    static {
        DEFAULT = new FoodProperties.Builder().nutrition(2).saturationMod(1.5F).build();
        ENCHANTED_BLUEBERRY = new FoodProperties.Builder().nutrition(8).saturationMod(10.0F).build();
        WHITE_APPLE = new FoodProperties.Builder().saturationMod(10.0F).alwaysEat().fast().build();
        HEALING_STONE = new FoodProperties.Builder().saturationMod(2.5F).alwaysEat().fast().effect(new MobEffectInstance(MobEffects.REGENERATION, 610, 0), 1.0F).build();
        MILK = new FoodProperties.Builder().nutrition(20).saturationMod(1F).fast().alwaysEat().effect(new MobEffectInstance(MobEffects.REGENERATION, 1200, 2), 1F).effect(new MobEffectInstance(MobEffects.ABSORPTION, 3600, 4), 1F).effect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 3600, 1), 1F).build();
    }
}