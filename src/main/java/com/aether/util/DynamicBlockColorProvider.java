package com.aether.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;

public interface DynamicBlockColorProvider {
    @Environment(EnvType.CLIENT)
    BlockColorProvider getProvider();
}
