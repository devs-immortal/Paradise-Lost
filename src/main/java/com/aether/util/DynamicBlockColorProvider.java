package com.aether.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.color.block.BlockColorProvider;

@Environment(EnvType.CLIENT)
public interface DynamicBlockColorProvider {

    BlockColorProvider getProvider();
}
