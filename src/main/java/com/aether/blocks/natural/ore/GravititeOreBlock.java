package com.aether.blocks.natural.ore;

import com.aether.blocks.FloatingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class GravititeOreBlock extends FloatingBlock {

    public GravititeOreBlock() {
        // Note: Any Blocks using this should also be appropriately tagged as follows:
        // - MINEABLE_WITH_PICKAXE
        // - NEEDS_IRON_TOOL
        super(false, BlockBehaviour.Properties.of(Material.STONE).strength(5.0F).sound(SoundType.STONE));
    }

//    @Override
//    protected void onStartFloating(FloatingBlockEntity entityIn) {
//        entityIn.dropItem = false;
//    }
}