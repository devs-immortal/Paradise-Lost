package net.id.paradise_lost;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class ModConstants {
    public static final String MOD_ID = "paradise_lost";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    private ModConstants() {}

    public static ResourceLocation id(String location) {
        if (location.contains(":")) {
            return ResourceLocation.parse(location);
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
