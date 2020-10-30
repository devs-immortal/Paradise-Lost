package com.aether.items;

import net.minecraft.item.FoodComponent;

public class AetherFood {
    public static final FoodComponent BLUE_BERRY;

    static {
        BLUE_BERRY = (new FoodComponent.Builder()).hunger(4).saturationModifier(0.3F).build();
    }
}
