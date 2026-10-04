package net.id.paradise_lost.util;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.id.paradise_lost.registry.ItemRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public final class MiscUtil {
    private MiscUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T dummyObject() {
        return (T) new Object();
    }

    public static <T> T deserializeDataJson(Codec<T> codec, ResourceLocation resource) throws IOException {
        return deserializeDataJson(JsonOps.INSTANCE, codec, resource);
    }

    public static <T> T deserializeDataJson(DynamicOps<JsonElement> ops, Codec<T> codec, ResourceLocation resource) throws IOException {
        var resourcePath = "/data/" + resource.getNamespace() + '/' + resource.getPath() + ".json";
        var stream = MiscUtil.class.getResourceAsStream(resourcePath);
        if (stream == null) {
            throw new FileNotFoundException("Failed to locate data JSON: " + resource);
        }
        try (stream) {
            var reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            var decodeResult = codec.decode(ops, GsonHelper.parse(reader));

            var result = decodeResult.result();
            if (result.isPresent()) {
                return result.get().getFirst();
            } else {

                throw new IOException(decodeResult.error().get().message());
            }
        }
    }

    public static boolean hasLevitationTotem(LivingEntity entity) {
        for (InteractionHand hand : InteractionHand.values()) {
            if (entity.getItemInHand(hand).is(ItemRegistry.TOTEM_OF_LEVITATION.get())) {
                return true;
            }
        }
        return false;
    }

    public static boolean useLevitationTotem(LivingEntity entity) {
        ItemStack itemStack = null;

        for (InteractionHand hand : InteractionHand.values()) {
            ItemStack itemStack2 = entity.getItemInHand(hand);
            if (itemStack2.is(ItemRegistry.TOTEM_OF_LEVITATION.get())) {
                itemStack = itemStack2.copy();
                itemStack2.shrink(1);

                if (entity instanceof ServerPlayer serverPlayerEntity) {
                    serverPlayerEntity.awardStat(Stats.ITEM_USED.get(ItemRegistry.TOTEM_OF_LEVITATION.get()));
                    CriteriaTriggers.USED_TOTEM.trigger(serverPlayerEntity, itemStack);
                    entity.gameEvent(GameEvent.ITEM_INTERACT_FINISH);
                }
                break;
            }
        }

        return itemStack != null;
    }
}
