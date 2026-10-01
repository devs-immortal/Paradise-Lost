package net.id.paradise_lost;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModConstants {
    public static final String MODID = "paradise_lost";
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    private static final ResourceLocation BASE_ID = ResourceLocation.fromNamespaceAndPath(MODID, "");

    private ModConstants() {}

    public static ResourceLocation id(String path) {
        return BASE_ID.withPath(path);
    }
}
