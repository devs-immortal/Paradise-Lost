package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class QuicksoilGlassPaneBlock extends IronBarsBlock {
    public QuicksoilGlassPaneBlock() {
        super(FabricBlockSettings.of(Material.GLASS).luminance(14).strength(0.2F, -1.0F).sound(SoundType.GLASS).noOcclusion().isRedstoneConductor(AetherBlocks::never));
    }
}