package net.id.aether.registry;

import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public final class AetherRegistryQueues {
    public static final RegistryQueue<Block> BLOCK = new RegistryQueue<>(Registry.BLOCK);
    public static final RegistryQueue<EntityType<?>> ENTITY_TYPE = new RegistryQueue<>(Registry.ENTITY_TYPE);
    public static final RegistryQueue<Item> ITEM = new RegistryQueue<>(Registry.ITEM);
    public static final RegistryQueue<Fluid> FLUID = new RegistryQueue<>(Registry.FLUID);
    public static final RegistryQueue<StatusEffect> STATUS_EFFECT = new RegistryQueue<>(Registry.STATUS_EFFECT);
}
