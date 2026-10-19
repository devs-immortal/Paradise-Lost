package net.id.paradise_lost;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class PLConstants {
    public static final String MOD_ID = "paradise_lost";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);
    private static final ResourceLocation BASE_ID = ResourceLocation.fromNamespaceAndPath(MOD_ID, "");

    private PLConstants() {}

    public static ResourceLocation id(String path) {
        return BASE_ID.withPath(path);
    }

    public static ResourceLocation locate(String location) {
        if (location.contains(":")) {
            return ResourceLocation.parse(location);
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
