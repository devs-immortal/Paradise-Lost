package com.aether.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColor;

public interface DynamicBlockColorProvider {
    @Environment(EnvType.CLIENT)
    BlockColor getProvider();
}
