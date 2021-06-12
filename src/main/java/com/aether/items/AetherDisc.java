package com.aether.items;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

public class AetherDisc extends RecordItem {
    public AetherDisc(int comparatorValueIn, SoundEvent soundIn, Properties settings) {
        super(comparatorValueIn, soundIn, settings/*new Settings().maxCount(1).group(AetherItemGroups.Misc).rarity(Rarity.RARE)*/);
    }
}