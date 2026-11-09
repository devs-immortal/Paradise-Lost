package net.id.paradise_lost.util;

import net.minecraft.world.level.material.MapColor;

public class ParadiseLostMapColorUtil {
    public static final MapColor PARADISE_LOST_BACKGROUND = new MapColor(62, 0xE3FFFD);

    public static void init() {
        // The MapColor constructor adds the id to vanilla's color table, so the client must load this class too.
    }
}
