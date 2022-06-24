package net.id.aether.blocks.decorative;

import net.id.aether.client.rendering.particle.AetherParticles;
import net.minecraft.block.TorchBlock;

public class AmbrosiumTorchBlock extends TorchBlock {
    public AmbrosiumTorchBlock(Settings settings) {
        super(settings, AetherParticles.AMBROSIUM_FLAME);
    }
}