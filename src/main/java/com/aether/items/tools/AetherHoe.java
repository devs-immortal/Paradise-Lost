package com.aether.items.tools;

import com.aether.items.AetherItemGroups;
import com.aether.items.utils.AetherTiers;
import net.minecraft.item.HoeItem;
import net.minecraft.util.Rarity;

public class AetherHoe extends HoeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherHoe(AetherTiers material, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, new Settings().group(AetherItemGroups.Tools));
        this.material = material;
    }

    public AetherHoe(AetherTiers material, Rarity rarity, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, new Settings().group(AetherItemGroups.Tools).rarity(rarity));
        this.material = material;
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}