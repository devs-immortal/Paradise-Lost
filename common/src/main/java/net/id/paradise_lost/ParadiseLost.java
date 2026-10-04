package net.id.paradise_lost;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

public final class ParadiseLost {
    public static final String MOD_ID = ModConstants.MODID;
    public static final Logger LOG = LogUtils.getLogger();

    private ParadiseLost() {}

    public static ResourceLocation locate(String location) {
        if (location.contains(":")) {
            return ResourceLocation.parse(location);
        }
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, location);
    }
}
