package com.aether.items.weapons;

import com.aether.mixin.item.SwordItemAccessor;
import com.google.common.collect.ImmutableMultimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;

import java.util.UUID;

public class ValkyrieLanceItem extends SwordItem {
    private static final UUID REACH_MODIFIER_ID = UUID.fromString("E70BE4E9-B163-4CC1-8848-F860B0BC02FC");
    private static final UUID ATTACK_RANGE_MODIFIER_ID = UUID.fromString("7CB7BC58-D3BA-40AE-BC95-F8C38fE144FF");

    public ValkyrieLanceItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, float reach, float attackRange, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.putAll(((SwordItemAccessor) this).getAttributeModifiers());
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_MODIFIER_ID, "Weapon modifier", reach, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(ATTACK_RANGE_MODIFIER_ID, "Weapon modifier", attackRange, EntityAttributeModifier.Operation.ADDITION));
        ((SwordItemAccessor) this).setAttributeModifiers(builder.build());
    }
}
