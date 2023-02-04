package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.block.TorchBlock;

public class CherineTorchBlock extends TorchBlock {
    public CherineTorchBlock(Settings settings) {
        super(settings, ParadiseLostParticles.CHERINE_FLAME);
    }
}
