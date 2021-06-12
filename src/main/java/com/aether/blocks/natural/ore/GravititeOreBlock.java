package com.aether.blocks.natural.ore;

import com.aether.blocks.FloatingBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public class GravititeOreBlock extends FloatingBlock {

    public GravititeOreBlock() {
        super(false, FabricBlockSettings.of(Material.STONE).strength(5.0F).breakByTool(FabricToolTags.PICKAXES, 2).sound(SoundType.STONE));
    }

//    @Override
//    protected void onStartFloating(FloatingBlockEntity entityIn) {
//        entityIn.dropItem = false;
//    }
}