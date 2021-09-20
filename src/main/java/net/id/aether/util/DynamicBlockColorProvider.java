package net.id.aether.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.color.block.BlockColorProvider;
import net.minecraft.client.option.GraphicsMode;
import net.minecraft.util.math.BlockPos;

public interface DynamicBlockColorProvider {
    @Environment(EnvType.CLIENT)
    BlockColorProvider getProvider();

    public static void handleFastGraphics(BlockPos pos){
        if (!isFastGraphics()) {
            MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    static boolean isFastGraphics(){
        return MinecraftClient.getInstance().options.graphicsMode.equals(GraphicsMode.FAST);
    }
}
