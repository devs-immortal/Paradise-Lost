package net.id.aether.blocks.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.id.aether.util.SimplexNoise;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public interface DynamicColorBlock {
    @Environment(EnvType.CLIENT)
    BlockColorProvider getBlockColorProvider();

    @Environment(EnvType.CLIENT)
    ItemColorProvider getBlockItemColorProvider();

    static void updateBlockColor(BlockPos pos){
        if (!isFastGraphics()) {
            MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    static boolean isFastGraphics(){
        return MinecraftClient.getInstance().options.getGraphicsMode().getValue().equals(GraphicsMode.FAST);
    }

    // Sigmoid
    static double contrastCurve(double contrast, double percent) {
        percent = percent - 0.5;
        return MathHelper.clamp(percent * Math.sqrt((4 + contrast) / (4 + 4 * contrast * percent * percent)) + 0.5, 0, 1);
    }

    // Output range: [0, 1]. clumpSize cannot be 0.
    static double sampleNoise(BlockPos pos, float clumpSize, float offset){
        return 0.5 * (1 + SimplexNoise.noise(pos.getX() / clumpSize + offset, pos.getY() / clumpSize + offset, pos.getZ() / clumpSize + offset));
    }
}
