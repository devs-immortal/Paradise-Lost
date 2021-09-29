package net.id.aether.items;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.id.aether.items.utils.AetherDispenserBehaviors;
import net.id.aether.items.utils.StackableVariantColorizer;
import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemConvertible;

class AetherItemActions {
    protected static final RegistryQueue.Action<ItemConvertible> compostable30 = compostable(0.3f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable50 = compostable(0.5f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable65 = compostable(0.65f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable100 = compostable(1f);

    protected static final RegistryQueue.Action<ItemConvertible> emptiableBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.emptiableBucket);
    protected static final RegistryQueue.Action<ItemConvertible> emptyBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.emptyBucket);
    protected static final RegistryQueue.Action<ItemConvertible> spawnEggBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.spawnEgg);

    protected static RegistryQueue.Action<ItemConvertible> fuel(int ticks) { return (id, item) -> FuelRegistry.INSTANCE.add(item, ticks);}
    protected static RegistryQueue.Action<ItemConvertible> compostable(float chance) { return (id, item) -> CompostingChanceRegistry.INSTANCE.add(item, chance); }

    protected static final RegistryQueue.Action<ItemConvertible> swetColor = RegistryQueue.onClient(new StackableVariantColorizer(0xDADADA, 0x939393, 0x4F4F4F));

}
