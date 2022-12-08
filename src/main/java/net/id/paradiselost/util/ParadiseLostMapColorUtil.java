package net.id.paradiselost.util;

import net.minecraft.block.MapColor;

public class ParadiseLostMapColorUtil {
    public static final MapColor PARADISE_LOST_BACKGROUND = MapColorCreator.createMapColor(62, 0xe3fffd);

    public static int getColor(MapColor material, int shade) {
        int newColor = recolor(material.id) == -1 ? material.color : recolor(material.id);

        int value = 220;

        // From here on out, standard brightness and color calculations.
        if (material == PARADISE_LOST_BACKGROUND) value = 220;
        else if (shade == 3) value = 135;
        else if (shade == 2) value = 255;
        else if (shade == 1) value = 220;
        else if (shade == 0) value = 180;

        int r = (newColor >> 16 & 255) * value / 255;
        int g = (newColor >> 8 & 255) * value / 255;
        int b = (newColor & 255) * value / 255;
        return -16777216 | b << 16 | g << 8 | r;
    }

    private static int recolor(int id) {
        return switch (id) {
            case 1 -> 0x72A684;     // PALE_GREEN
            case 5 -> 0x62767E;     // PALE_PURPLE
            case 7 -> 0x7EA651;     // DARK_GREEN
            case 12 -> 0x76ACB2;    // WATER_BLUE
            case 30 -> 0xAAAB3C;    // GOLD
            default -> -1;         // No special color tweaking.
        };
    }
}
