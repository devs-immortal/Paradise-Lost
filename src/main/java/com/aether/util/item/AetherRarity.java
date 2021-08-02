package com.aether.util.item;

import com.aether.util.EnumExtender;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class AetherRarity {
    public static Rarity AETHER_LOOT = EnumExtender.add(Rarity.class, "AETHER_LOOT", Formatting.GREEN);
}
