package com.aether.blocks.natural.ore;

import com.aether.blocks.FloatingBlock;
import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;

public class GravititeOreBlock extends FloatingBlock {

    public GravititeOreBlock() {
        super(false, FabricBlockSettings.of(Material.STONE).strength(5.0F).sounds(BlockSoundGroup.STONE).breakByTool(FabricToolTags.PICKAXES, 2));
    }

    @Override
    protected void onStartFloating(FloatingBlockEntity entityIn) {
        entityIn.dropItem = false;
    }
}