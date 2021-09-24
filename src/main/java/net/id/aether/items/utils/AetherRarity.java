package net.id.aether.items.utils;

import net.id.aether.util.EnumExtender;
import net.minecraft.util.Formatting;
import net.minecraft.util.Rarity;

public class AetherRarity {
    public static Rarity AETHER_LOOT = EnumExtender.add(Rarity.class, "AETHER_LOOT", Formatting.GREEN);
}
