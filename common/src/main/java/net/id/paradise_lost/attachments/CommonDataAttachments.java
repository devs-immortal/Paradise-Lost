package net.id.paradise_lost.attachments;

import net.id.paradise_lost.platform.Services;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommonDataAttachments {

    private static final Map<ResourceLocation, CommonDataAttachment<?>> MAP = new HashMap<>();

    public static final FloatyMinecartAttachment FLOATY_MINECART = register(new FloatyMinecartAttachment());

    public static final AnchoringAttachment ANCHORING = register(new AnchoringAttachment());

    public static CommonDataAttachment<?> lookup(ResourceLocation location) {
        return MAP.get(location);
    }

    public static void init() {
        Objects.requireNonNull(FLOATY_MINECART.getName());
        Objects.requireNonNull(ANCHORING.getName());
    }

    static <T, A extends CommonDataAttachment<T>> A register(A type) {
        Services.ATTACHMENTS.registerDataAttachment(type);
        Objects.requireNonNull(type.getAttachment());
        MAP.put(type.getName(), type);
        return type;
    }
}
