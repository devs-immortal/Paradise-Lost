package net.id.paradiselost.blocks.natural.tree;

import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.entities.hostile.swet.TransformableSwetEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MotherAurelLogBlock extends PillarBlock {
    public MotherAurelLogBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        super.onEntityCollision(state, world, pos, entity);
        if ((this == ParadiseLostBlocks.MOTHER_AUREL_LOG)
                && entity instanceof TransformableSwetEntity swet) {
            swet.suggestTypeChange(state);
        }
    }
}
