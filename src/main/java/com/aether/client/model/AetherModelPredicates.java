package com.aether.client.model;

import com.aether.Aether;
import com.aether.items.AetherItems;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.client.item.UnclampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class AetherModelPredicates {

    public static void init() {
        FabricModelPredicateProviderRegistry.register(AetherItems.PHOENIX_BOW, Aether.locate("pull"), ((stack, world, entity, seed) -> {
            if (entity == null) {
                return 0F;
            }
            return entity.getActiveItem() != stack ? 0F : (stack.getMaxUseTime() - entity.getItemUseTimeLeft()) / 20F;
        }));

        FabricModelPredicateProviderRegistry.register(AetherItems.PHOENIX_BOW, Aether.locate("pulling"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            }
            return livingEntity.isUsingItem() && livingEntity.getActiveItem() == itemStack ? 1.0F : 0.0F;
        });

        FabricModelPredicateProviderRegistry.register(Aether.locate("stackable_variant"), (new UnclampedModelPredicateProvider() {
            @SuppressWarnings("deprecation")
            @Override
            public float call(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
                return unclampedCall(stack, world, entity, seed);
            }

            @Override
            public float unclampedCall(ItemStack stack, @Nullable ClientWorld world, @Nullable LivingEntity entity, int seed) {
                return stack.getSubNbt("stackableVariant") != null ? stack.getSubNbt("stackableVariant").getInt("variant") : 0;
            }
        }));
    }
}
