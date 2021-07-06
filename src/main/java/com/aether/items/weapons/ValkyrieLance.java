package com.aether.items.weapons;

import com.aether.items.tools.AetherSword;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;

import java.util.UUID;

public class ValkyrieLance extends AetherSword {

    private static final UUID REACH_ID = UUID.fromString("FA234E1C-4180-4865-B01B-BCCE8785ACA3");
    private static final UUID ATTACK_ID = UUID.fromString("FA234E1C-5180-4765-C01B-BCCE8785ACA3");
    private final Multimap<EntityAttribute, EntityAttributeModifier> attributes;

    public ValkyrieLance(Settings settings) {
        super(AetherTiers.VALKYRIE, -3.0F, 6, settings);
        ImmutableMultimap.Builder<EntityAttribute, EntityAttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_DAMAGE_MODIFIER_ID, "Weapon modifier", 10.0, EntityAttributeModifier.Operation.ADDITION));
        builder.put(EntityAttributes.GENERIC_ATTACK_SPEED, new EntityAttributeModifier(ATTACK_SPEED_MODIFIER_ID, "Weapon modifier", -3.0F, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.REACH, new EntityAttributeModifier(REACH_ID, "Weapon modifier", 6.0, EntityAttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new EntityAttributeModifier(ATTACK_ID, "Weapon modifier", 4.0, EntityAttributeModifier.Operation.ADDITION));
        this.attributes = builder.build();
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? attributes : super.getAttributeModifiers(slot);
    }
}
