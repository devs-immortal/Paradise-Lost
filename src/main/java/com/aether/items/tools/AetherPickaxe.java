package com.aether.items.tools;

import com.aether.items.AetherItemGroups;
import com.aether.items.utils.AetherTiers;
import net.minecraft.item.PickaxeItem;
import net.minecraft.util.Rarity;

public class AetherPickaxe extends PickaxeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherPickaxe(AetherTiers material, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.TOOLS));
        this.material = material;
    }

    public AetherPickaxe(AetherTiers material, Rarity rarity, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, new Settings().group(AetherItemGroups.TOOLS).rarity(rarity));
        this.material = material;
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }

}