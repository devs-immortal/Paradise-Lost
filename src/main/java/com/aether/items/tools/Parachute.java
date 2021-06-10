package com.aether.items.tools;

import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotAttributes;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;

import java.util.UUID;

// TODO: Parachute Logic in need of rewrites for Trinkets v3.0.0 rewrite
public class Parachute extends TrinketItem {

    public Parachute(Settings settings) {
        super(settings);
    }

    @Override
    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);
        // +10% movement speed
        //modifiers.put(EntityAttributes.GENERIC_MOVEMENT_SPEED, new EntityAttributeModifier(uuid, "guidemod:movement_speed", 0.1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
        // If the player has access to ring slots, this will give them an extra one
        //SlotAttributes.addSlotModifier(modifiers, "hand/ring", uuid, 1, EntityAttributeModifiers.Operation.ADDITION);
        return modifiers;
    }
}