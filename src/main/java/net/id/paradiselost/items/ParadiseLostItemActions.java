package net.id.paradiselost.items;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.id.paradiselost.items.utils.ParadiseLostDispenserBehaviors;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemConvertible;

import java.util.function.Consumer;

class ParadiseLostItemActions {
    protected static final Consumer<ItemConvertible> compostable15 = compostable(0.15f);
    protected static final Consumer<ItemConvertible> compostable30 = compostable(0.3f);
    protected static final Consumer<ItemConvertible> compostable50 = compostable(0.5f);
    protected static final Consumer<ItemConvertible> compostable65 = compostable(0.65f);
    protected static final Consumer<ItemConvertible> compostable85 = compostable(0.85f);
    protected static final Consumer<ItemConvertible> compostable100 = compostable(1f);

    protected static final Consumer<ItemConvertible> emptiableBucketBehavior = (item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptiableBucket);
    protected static final Consumer<ItemConvertible> emptyBucketBehavior = (item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptyBucket);
    protected static final Consumer<ItemConvertible> spawnEggBehavior = (item) -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.spawnEgg);

    protected static Consumer<ItemConvertible> fuel(int ticks) {
        return (item) -> FuelRegistry.INSTANCE.add(item, ticks);
    }
    protected static Consumer<ItemConvertible> compostable(float chance) {
        return (item) -> CompostingChanceRegistry.INSTANCE.add(item, chance);
    }


}
