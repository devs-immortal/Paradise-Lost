package net.id.paradise_lost.attachments;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.platform.Services;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation, CommonDataAttachment<?>> MAP = new HashMap<>();

    public static final CommonDataAttachment<Integer> MINE_CART_FLOATING =
            register(CommonDataAttachment.create(o -> 0)
                    .codec(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build("minecart_floating_time"));

    public static final CommonDataAttachment<Boolean> FLOATY_ANCHORED =
            register(CommonDataAttachment.create(o -> false)
                    .codec(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build("floaty_anchored"));

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }
    public static void init() {
        Objects.requireNonNull(MINE_CART_FLOATING.getName());
        Objects.requireNonNull(FLOATY_ANCHORED.getName());
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.ATTACHMENTS.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.getName(), type);
        return type;
    }
}
