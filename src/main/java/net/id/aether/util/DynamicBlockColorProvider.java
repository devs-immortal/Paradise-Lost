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

    default void handleFabulousGraphics(BlockPos pos){
        if (isFabulousGraphics()) {
            MinecraftClient.getInstance().worldRenderer.scheduleBlockRenders(pos.getX(), pos.getY(), pos.getZ(), pos.getX(), pos.getY(), pos.getZ());
        }
    }

    static boolean isFabulousGraphics(){
        return MinecraftClient.getInstance().options.graphicsMode.equals(GraphicsMode.FABULOUS);
    }
}
