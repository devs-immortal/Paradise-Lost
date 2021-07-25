package com.aether.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.ConditionalLootFunction;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.util.JsonHelper;

import java.util.Set;

public class SetStackableVariantLootFunction extends ConditionalLootFunction {
    final LootNumberProvider variant;

    public SetStackableVariantLootFunction(LootCondition[] conditions, LootNumberProvider variant) {
        super(conditions);
        this.variant = variant;
    }

    @Override
    public LootFunctionType getType() {
        return AetherLootFunctionTypes.SET_STACKABLE_VARIANT;
    }

    @Override
    public Set<LootContextParameter<?>> getRequiredParameters() {
        return this.variant.getRequiredParameters();
    }

    @Override
    protected ItemStack process(ItemStack stack, LootContext context) {
        stack.getOrCreateNbt().putInt("stackableVariant", variant.nextInt(context));
        return stack;
    }

    public static class Serializer extends ConditionalLootFunction.Serializer<SetStackableVariantLootFunction> {
        @Override
        public void toJson(JsonObject json, SetStackableVariantLootFunction function, JsonSerializationContext context) {
            super.toJson(json, function, context);
            json.add("variant", context.serialize(function.variant));
        }

        @Override
        public SetStackableVariantLootFunction fromJson(JsonObject json, JsonDeserializationContext context, LootCondition[] conditions) {
            return new SetStackableVariantLootFunction(conditions, JsonHelper.deserialize(json, "variant", context, LootNumberProvider.class));
        }
    }
}