package com.aether.blocks.decorative;

import com.aether.blocks.AetherBlocks;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Material;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.BlockSoundGroup;

public class AmbrosiumTorchWallBlock extends WallTorchBlock {
    public AmbrosiumTorchWallBlock() {
        super(FabricBlockSettings.of(Material.SUPPORTED).collidable(false).breakByHand(true).ticksRandomly().lightLevel(15).sounds(BlockSoundGroup.WOOD).dropsLike(AetherBlocks.AMBROSIUM_TORCH), ParticleTypes.FLAME);
    }
}
