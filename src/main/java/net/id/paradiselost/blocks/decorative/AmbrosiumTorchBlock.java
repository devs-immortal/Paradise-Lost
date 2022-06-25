package net.id.paradiselost.blocks.decorative;

import net.id.paradiselost.client.rendering.particle.ParadiseLostParticles;
import net.minecraft.block.TorchBlock;

public class AmbrosiumTorchBlock extends TorchBlock {
    public AmbrosiumTorchBlock(Settings settings) {
        super(settings, ParadiseLostParticles.AMBROSIUM_FLAME);
    }
}
