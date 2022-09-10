package net.id.paradiselost.blocks.natural.plant;

import net.minecraft.block.SeagrassBlock;

public class ParadiseLostSeagrassBlock extends SeagrassBlock {

    public ParadiseLostSeagrassBlock(Settings settings) {
        super(settings.offsetType(OffsetType.XZ));
    }

    @Override
    public float getMaxHorizontalModelOffset() {
        return 0.125F;
    }
}
