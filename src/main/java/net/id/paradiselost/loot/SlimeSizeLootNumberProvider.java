package net.id.paradiselost.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.JsonSerializer;

public class SlimeSizeLootNumberProvider implements LootNumberProvider {
    final LootNumberProvider multiplier;

    public SlimeSizeLootNumberProvider(LootNumberProvider multiplier) {
        this.multiplier = multiplier;
    }

    @Override
    public LootNumberProviderType getType() {
        return ParadiseLostLootNumberProviderTypes.SLIME_SIZE;
    }

    @Override
    public float nextFloat(LootContext context) {
        if (context.get(LootContextParameters.THIS_ENTITY) instanceof SlimeEntity slime) {
            return slime.getSize() * multiplier.nextFloat(context);
        }
        return 0;
    }

    public static class Serializer implements JsonSerializer<SlimeSizeLootNumberProvider> {
        @Override
        public SlimeSizeLootNumberProvider fromJson(JsonObject json, JsonDeserializationContext context) {
            return new SlimeSizeLootNumberProvider(JsonHelper.deserialize(json, "multiplier", context, LootNumberProvider.class));
        }

        @Override
        public void toJson(JsonObject json, SlimeSizeLootNumberProvider object, JsonSerializationContext context) {
            json.add("multiplier", context.serialize(object.multiplier));
        }
    }
}
