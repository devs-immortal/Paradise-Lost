package com.aether.client.rendering.map;

import net.minecraft.block.MapColor;

public class AetherMap {

    public static int getColor(MapColor material, int shade) {
        int newColor = recolor(material.id) == -1 ? material.color : recolor(material.id);

        int value = 220;

        if (shade == 3) {
            value = 135;
        }

        if (shade == 2) {
            value = 255;
        }

        if (shade == 1) {
            value = 220;
        }

        if (shade == 0) {
            value = 180;
        }

        int r = (newColor >> 16 & 255) * value / 255;
        int g = (newColor >> 8 & 255) * value / 255;
        int b = (newColor & 255) * value / 255;

        return -16777216 | b << 16 | g << 8 | r;
    }

    private static int recolor(int id) {
        if (id == 1) {
            return 0x72A684;
        } else if (id == 5) {
            return 0x62767E;
        } else if (id == 7) {
            return 0x7EA651;
        } else if (id == 12) {
            return 0x76acb2;
        } else if (id == 30) {
            return 0xAAAB3C;
        }

        return -1;
    }
}
