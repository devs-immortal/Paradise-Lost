package com.aether.items;

import net.minecraft.item.MusicDiscItem;
import net.minecraft.sound.SoundEvent;

public class AetherDisc extends MusicDiscItem {
    public AetherDisc(int comparatorValueIn, SoundEvent soundIn, Settings settings) {
        super(comparatorValueIn, soundIn, settings/*new Settings().maxCount(1).group(AetherItemGroups.Misc).rarity(Rarity.RARE)*/);
    }
}