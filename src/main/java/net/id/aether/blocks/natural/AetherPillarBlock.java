package net.id.aether.blocks.natural;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.hostile.swet.TransformableSwetEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AetherPillarBlock extends PillarBlock {
    public AetherPillarBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if ((this == AetherBlocks.GOLDEN_OAK_LOG)
                && entity instanceof TransformableSwetEntity swet) {
            swet.suggestTypeChange(state);
        }
    }
}
