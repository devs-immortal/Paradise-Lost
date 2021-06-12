package com.aether.items.weapons;

import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class ValkyrieLance extends AetherSword {

    private static final UUID REACH_ID = UUID.fromString("FA234E1C-4180-4865-B01B-BCCE8785ACA3");
    private static final UUID ATTACK_ID = UUID.fromString("FA234E1C-5180-4765-C01B-BCCE8785ACA3");
    private final Multimap<Attribute, AttributeModifier> attributes;

    public ValkyrieLance(Properties settings) {
        super(AetherTiers.Valkyrie, -3.0F, 6, settings);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 10.0, AttributeModifier.Operation.ADDITION));
        builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.0F, AttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.REACH, new AttributeModifier(REACH_ID, "Weapon modifier", 6.0, AttributeModifier.Operation.ADDITION));
        builder.put(ReachEntityAttributes.ATTACK_RANGE, new AttributeModifier(ATTACK_ID, "Weapon modifier", 4.0, AttributeModifier.Operation.ADDITION));
        this.attributes = builder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        return slot == EquipmentSlot.MAINHAND ? attributes : super.getDefaultAttributeModifiers(slot);
    }
}
