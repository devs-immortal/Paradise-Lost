package com.aether.mixin.entity;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MixinAbstractFurnaceBlockEntity {
    @Inject(method = "createFuelTimeMap", at = @At("RETURN"), cancellable = true)
    private static void fuelTimeMapHook(CallbackInfoReturnable<Map<Item, Integer>> info) {
        // Format: item, fuelTime
        final Map<Item, Integer> returnValue = info.getReturnValue();

        returnValue.put(AetherItems.AMBROSIUM_SHARD, 500);
        returnValue.put(AetherItems.SKYROOT_BUCKET, 200);
        returnValue.put(AetherBlocks.FOOD_BOWL.asItem(), 300);
        returnValue.put(AetherBlocks.INCUBATOR.asItem(), 300);

        info.setReturnValue(returnValue);
    }
}
