package net.id.aether.blocks;

import net.minecraft.block.SeagrassBlock;

public class AetherSeagrassBlock extends SeagrassBlock {

    public AetherSeagrassBlock(Settings settings) {
        super(settings);
    }

    @Override
    public OffsetType getOffsetType() {
        return OffsetType.XZ;
    }

    @Override
    public float getMaxModelOffset() {
        return 0.125F;
    }
}
