package net.id.paradise_lost.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

@SuppressWarnings("unused")
public class ParadiseLostFoodComponent {
    public static final FoodProperties BLACKCURRANT = new FoodProperties.Builder().nutrition(2).saturationModifier(0.5F).fast().build();
    public static final FoodProperties MOA_MEAT = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build();
    public static final FoodProperties COOKED_MOA_MEAT = new FoodProperties.Builder().nutrition(6).saturationModifier(1F).build();
    public static final FoodProperties POPOM_JELLY = new FoodProperties.Builder().nutrition(1).saturationModifier(1.0F).alwaysEdible()
            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).build();
    public static final FoodProperties SWEDROOT = new FoodProperties.Builder().nutrition(2).saturationModifier(1.5F).build();
    public static final FoodProperties GENERIC_WORSE = new FoodProperties.Builder().nutrition(1).saturationModifier(0.25F)
            .effect(new MobEffectInstance(MobEffects.CONFUSION, 100, 0), 0.075F).build();
    public static final FoodProperties AMADRYS_NOODLES = new FoodProperties.Builder().nutrition(7).saturationModifier(0.5F).usingConvertsTo(Items.BOWL).build();
    public static final FoodProperties AMADRYS_BREAD = new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build();
    public static final FoodProperties AMADRYS_BREAD_GLAZED = new FoodProperties.Builder().nutrition(8).saturationModifier(1.4F).build();
    public static final FoodProperties AMADRYS_BREAD_GLAZED_FILLED = new FoodProperties.Builder().nutrition(9).saturationModifier(1.4F)
            .effect(new MobEffectInstance(MobEffects.HEAL, 1, 0), 1.0F).build();
    public static final FoodProperties BLACKCURRANT_PIE = new FoodProperties.Builder().nutrition(9).saturationModifier(0.2F).build();
    public static final FoodProperties BLACKCURRANT_COOKIE = new FoodProperties.Builder().nutrition(5).saturationModifier(1.2F).build();
    /** Matches vanilla cookie nutrition / saturation; Bad Omen is applied from the stack's ominous amplifier. */
    public static final FoodProperties OMINOUS_COOKIE = new FoodProperties.Builder().nutrition(2).saturationModifier(0.1F).alwaysEdible().build();
    public static final FoodProperties ROOT_STEW = new FoodProperties.Builder().nutrition(10).saturationModifier(1.5F).usingConvertsTo(Items.BOWL).build();
}
