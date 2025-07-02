package net.id.paradiselost.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.id.paradiselost.items.ParadiseLostItems;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.event.GameEvent;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class MiscUtil {
    private MiscUtil() {
    }

    /**
     * Should only be used by methods that are never actually called, used to make Accessors less painful.
     */
    @SuppressWarnings("unchecked")
    public static <T> T dummyObject() {
        return (T) new Object();
    }

    /**
     * Deserializes a JSON file that is stored in the "data" directory of this mod.
     *
     * @param codec    The coded to use to deserialize the JSON
     * @param resource The identifier of the JSON
     * @param <T>      The type of data to deserialize
     * @return The deserialized JSON
     * @throws IOException If there was a problem reading or parsing the JSON
     */
    public static <T> T deserializeDataJson(Codec<T> codec, Identifier resource) throws IOException {
        return deserializeDataJson(JsonOps.INSTANCE, codec, resource);
    }

    /**
     * Deserializes a JSON file that is stored in the "data" directory of this mod.
     *
     * @param ops      The ops to use instead of the default JSON ops
     * @param codec    The coded to use to deserialize the JSON
     * @param resource The identifier of the JSON
     * @param <T>      The type of data to deserialize
     * @return The deserialized JSON
     * @throws IOException If there was a problem reading or parsing the JSON
     */
    public static <T> T deserializeDataJson(DynamicOps<JsonElement> ops, Codec<T> codec, Identifier resource) throws IOException {
        var resourcePath = "/data/" + resource.getNamespace() + '/' + resource.getPath() + ".json";
        var stream = MiscUtil.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new FileNotFoundException("Failed to locate data JSON: " + resource);
        }
        try (stream) {
            var reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            var decodeResult = codec.decode(ops, JsonHelper.deserialize(reader));

            var result = decodeResult.result();
            if (result.isPresent()) {
                return result.get().getFirst();
            } else {
                //noinspection OptionalGetWithoutIsPresent
                throw new IOException(decodeResult.error().get().message());
            }
        }
    }

    public static boolean useLevitationTotem(LivingEntity entity) {
        ItemStack itemStack = null;

        for (Hand hand : Hand.values()) {
            ItemStack itemStack2 = entity.getStackInHand(hand);
            if (itemStack2.isOf(ParadiseLostItems.TOTEM_OF_LEVITATION)) {
                itemStack = itemStack2.copy();
                itemStack2.decrement(1);
                // advancement logic
                if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(ParadiseLostItems.TOTEM_OF_LEVITATION));
                    Criteria.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                    entity.emitGameEvent(GameEvent.ITEM_INTERACT_FINISH);
                }
                break;
            }
        }

        return itemStack != null;
    }
}
