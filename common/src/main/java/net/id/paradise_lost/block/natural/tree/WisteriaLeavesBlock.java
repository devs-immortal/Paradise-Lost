package net.id.paradise_lost.block.natural.tree;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;

public class WisteriaLeavesBlock extends ParadiseLostLeavesBlock {

    public WisteriaLeavesBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        if (state.getValue(PERSISTENT)) {
            return;
        }
        Set<BlockPos> checkedBlocks = new HashSet<>();
        Queue<BlockPos> next = new LinkedList<>();
        next.add(pos);

        while (!next.isEmpty() && next.size() < 1000) {
            var checkPos = next.poll();
            checkedBlocks.add(checkPos);

            if (world.getBlockState(checkPos).is(BlockTags.LOGS)) {
                return;
            }

            for (Direction direction : UPDATE_SHAPE_ORDER) {
                var nextPos = checkPos.relative(direction);
                if (!checkedBlocks.contains(nextPos) && world.getBlockState(nextPos).getBlock() instanceof WisteriaLeavesBlock) {
                    next.add(nextPos);
                }
            }
        }

        super.randomTick(state, world, pos, random);
    }
}
