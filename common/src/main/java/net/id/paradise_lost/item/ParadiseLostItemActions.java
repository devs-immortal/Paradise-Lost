package net.id.paradise_lost.item;

import net.id.paradise_lost.platform.Services;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.DispenserBlock;

import java.util.function.Consumer;

public class ParadiseLostItemActions {
    public static final Consumer<ItemLike> compostable15 = compostable(0.15f);
    public static final Consumer<ItemLike> compostable30 = compostable(0.3f);
    public static final Consumer<ItemLike> compostable50 = compostable(0.5f);
    public static final Consumer<ItemLike> compostable65 = compostable(0.65f);
    public static final Consumer<ItemLike> compostable85 = compostable(0.85f);
    public static final Consumer<ItemLike> compostable100 = compostable(1f);

    public static final Consumer<ItemLike> emptiableBucketBehavior =
            item -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptiableBucket);
    public static final Consumer<ItemLike> emptyBucketBehavior =
            item -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.emptyBucket);
    public static final Consumer<ItemLike> spawnEggBehavior =
            item -> DispenserBlock.registerBehavior(item, ParadiseLostDispenserBehaviors.spawnEgg);
    public static final Consumer<ItemLike> projectileBehavior = DispenserBlock::registerProjectileBehavior;

    public static Consumer<ItemLike> fuel(int ticks) {
        return item -> Services.REGISTRATION.registerFuel(item, ticks);
    }

    public static Consumer<ItemLike> compostable(float chance) {
        return item -> Services.REGISTRATION.registerCompostable(item, chance);
    }

    private ParadiseLostItemActions() {}
}
