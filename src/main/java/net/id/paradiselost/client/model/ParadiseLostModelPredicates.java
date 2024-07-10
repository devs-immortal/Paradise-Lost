package net.id.paradiselost.client.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.id.paradiselost.ParadiseLost;
import net.minecraft.client.item.ClampedModelPredicateProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class ParadiseLostModelPredicates {

    public static void initClient() {

        FabricModelPredicateProviderRegistry.register(ParadiseLost.locate("stackable_variant"), (new ClampedModelPredicateProvider() {
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
