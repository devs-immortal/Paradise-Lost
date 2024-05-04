package net.id.paradiselost.items;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.id.paradiselost.items.utils.ParadiseLostDispenserBehaviors;
import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemConvertible;

class ParadiseLostItemActions {
    protected static final RegistryQueue.Action<ItemConvertible> compostable15 = compostable(0.15f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable30 = compostable(0.3f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable50 = compostable(0.5f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable65 = compostable(0.65f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable85 = compostable(0.85f);
    protected static final RegistryQueue.Action<ItemConvertible> compostable100 = compostable(1f);

    protected static final RegistryQueue.Action<ItemConvertible> emptiableBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptiableBucket);
    protected static final RegistryQueue.Action<ItemConvertible> emptyBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptyBucket);
    protected static final RegistryQueue.Action<ItemConvertible> spawnEggBehavior = (id, item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.spawnEgg);

    protected static RegistryQueue.Action<ItemConvertible> fuel(int ticks) {
        return (id, item) -> FuelRegistry.INSTANCE.add(item, ticks);
    }
    protected static RegistryQueue.Action<ItemConvertible> compostable(float chance) {
        return (id, item) -> CompostingChanceRegistry.INSTANCE.add(item, chance);
    }

}
