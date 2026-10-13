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

    public static final CommonDataAttachment<Integer> MINE_CART_FLOAT_INCLINE =
            register(CommonDataAttachment.create(o -> 0)
                    .codec(Codec.INT)
                    .networkSynchronized(ByteBufCodecs.VAR_INT)
                    .build("minecart_float_incline"));

    public static final CommonDataAttachment<String> MINE_CART_FLOAT_SHAPE =
            register(CommonDataAttachment.create(o -> "")
                    .codec(Codec.STRING)
                    .networkSynchronized(ByteBufCodecs.STRING_UTF8)
                    .build("minecart_float_shape"));

    public static final CommonDataAttachment<Boolean> MINE_CART_FLOAT_OFF_RAIL =
            register(CommonDataAttachment.create(o -> false)
                    .codec(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build("minecart_float_off_rail"));

    public static final CommonDataAttachment<Float> MINE_CART_FLOAT_YAW =
            register(CommonDataAttachment.create(o -> 0.0F)
                    .codec(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .build("minecart_float_yaw"));

    public static final CommonDataAttachment<Float> MINE_CART_FLOAT_PITCH =
            register(CommonDataAttachment.create(o -> 0.0F)
                    .codec(Codec.FLOAT)
                    .networkSynchronized(ByteBufCodecs.FLOAT)
                    .build("minecart_float_pitch"));

    public static final CommonDataAttachment<Boolean> FLOATY_ANCHORED =
            register(CommonDataAttachment.create(o -> false)
                    .codec(Codec.BOOL)
                    .networkSynchronized(ByteBufCodecs.BOOL)
                    .build("floaty_anchored"));

    public static final CommonDataAttachment<Boolean> FLOATY_ANCHORED_SAVED =
            register(CommonDataAttachment.create(o -> false)
                    .codec(Codec.BOOL)
                    .build("floaty_anchored_saved"));

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }
    public static void init() {
        Objects.requireNonNull(MINE_CART_FLOATING.getName());
        Objects.requireNonNull(MINE_CART_FLOAT_INCLINE.getName());
        Objects.requireNonNull(MINE_CART_FLOAT_SHAPE.getName());
        Objects.requireNonNull(MINE_CART_FLOAT_OFF_RAIL.getName());
        Objects.requireNonNull(MINE_CART_FLOAT_YAW.getName());
        Objects.requireNonNull(MINE_CART_FLOAT_PITCH.getName());
        Objects.requireNonNull(FLOATY_ANCHORED.getName());
        Objects.requireNonNull(FLOATY_ANCHORED_SAVED.getName());
    }

    static <T> CommonDataAttachment<T> register(CommonDataAttachment<T> type) {
        Services.ATTACHMENTS.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.getName(), type);
        return type;
    }
}
