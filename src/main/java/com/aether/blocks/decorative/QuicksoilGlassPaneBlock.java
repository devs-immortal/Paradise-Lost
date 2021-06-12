package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class QuicksoilGlassPaneBlock extends IronBarsBlock {
    public QuicksoilGlassPaneBlock() {
        super(BlockBehaviour.Properties.of(Material.GLASS).lightLevel(state -> 14).strength(0.2F, -1.0F).sound(SoundType.GLASS).noOcclusion().isRedstoneConductor(AetherBlocks::never));
    }
}