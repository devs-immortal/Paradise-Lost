package net.id.aether.registry;

import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.util.registry.Registry;

public final class AetherRegistryQueues {
    public static final RegistryQueue<Block> BLOCK = new RegistryQueue<>(Registry.BLOCK, 256);
    public static final RegistryQueue<EntityType<?>> ENTITY_TYPE = new RegistryQueue<>(Registry.ENTITY_TYPE, 32);
    public static final RegistryQueue<Item> ITEM = new RegistryQueue<>(Registry.ITEM, 384);
}
