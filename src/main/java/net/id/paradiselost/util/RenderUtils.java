package net.id.paradiselost.util;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3i;

public class RenderUtils {

    public static int toHex(Vec3i color) {
        return MathHelper.packRgb(color.getX(), color.getY(), color.getZ());
    }

    @Deprecated(forRemoval = true)
    public static int toHex(int r, int g, int b) {
        return MathHelper.packRgb(r, g, b);
    }
    
    @Deprecated(forRemoval = true)
    public static int toHex(int r, int g, int b, int a) {
        return MathHelper.packRgb(r, g, b) | (a << 24);
    }

    public static Vec3i toRGB(int hex) {
        return new Vec3i((hex & 0xFF0000) >> 16, (hex & 0xFF00) >> 8, (hex & 0xFF));
    }

    public static void blockRenderLayer(Block block, RenderLayer layer) {
        BlockRenderLayerMap.INSTANCE.putBlock(block, layer);
    }

    public static void fluidRenderLayer(Fluid fluid, RenderLayer layer) {
        BlockRenderLayerMap.INSTANCE.putFluid(fluid, layer);
    }

    public static void transparentRenderLayer(Block block) {
        blockRenderLayer(block, RenderLayer.getTranslucent());
    }

    public static void transparentRenderLayer(Fluid fluid) {
        fluidRenderLayer(fluid, RenderLayer.getTranslucent());
    }

    public static void cutoutRenderLayer(Block block) {
        blockRenderLayer(block, RenderLayer.getCutout());
    }

    public static void cutoutMippedRenderLayer(Block block) {
        blockRenderLayer(block, RenderLayer.getCutoutMipped());
    }
}
