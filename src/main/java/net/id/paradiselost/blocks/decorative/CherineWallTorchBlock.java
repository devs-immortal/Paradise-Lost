package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.block.WallTorchBlock;

public class CherineWallTorchBlock extends WallTorchBlock {
    public CherineWallTorchBlock(Settings settings) {
        super(settings, ParadiseLostParticles.CHERINE_FLAME);
    }
}
