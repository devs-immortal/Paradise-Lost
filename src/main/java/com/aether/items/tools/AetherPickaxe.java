package com.aether.items.tools;

import com.aether.items.utils.AetherTiers;
import net.minecraft.item.PickaxeItem;

public class AetherPickaxe extends PickaxeItem implements IAetherTool {

    private final AetherTiers material;

    public AetherPickaxe(AetherTiers material, Settings settings, int damageVsEntity, float attackSpeed) {
        super(material.getDefaultTier(), damageVsEntity, attackSpeed, settings);
        this.material = material;
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}