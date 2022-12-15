package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.block.WallTorchBlock;

public class AmbrosiumWallTorchBlock extends WallTorchBlock {
    public AmbrosiumWallTorchBlock(Settings settings) {
        super(settings, ParadiseLostParticles.AMBROSIUM_FLAME);
    }
}
