package net.id.aether.util;

import net.minecraft.util.math.Vec3i;

public class RenderUtils {

    public static int toHex(Vec3i color) {
        return toHex(color.getX(), color.getY(), color.getZ());
    }

    public static int toHex(int r, int g, int b) {
        return 255 << 24 | (r << 16) | (g << 8) | b;
    }

    public static int toHex(int r, int g, int b, int a) {
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    public static Vec3i toRGB(int hex) {
        return new Vec3i((hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, (hex & 0xFF));
    }
}
