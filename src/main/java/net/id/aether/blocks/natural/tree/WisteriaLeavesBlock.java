package net.id.aether.blocks.natural.tree;

import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.*;

public class WisteriaLeavesBlock extends AetherLeavesBlock{

    public WisteriaLeavesBlock(Settings settings, boolean collidable) {
        super(settings, collidable);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(state.get(PERSISTENT)) {
            return;
        }
        Set<BlockPos> checkedBlocks = new HashSet<>();
        Queue<BlockPos> next = new LinkedList<>();
        next.add(pos);

        while(!next.isEmpty() && next.size() < 1000) {
            var checkPos = next.poll();
            checkedBlocks.add(checkPos);

            if(world.getBlockState(checkPos).isIn(BlockTags.LOGS)) {
                return;
            }

            for (Direction direction : DIRECTIONS) {
                var nextPos = checkPos.offset(direction);
                if(!checkedBlocks.contains(nextPos) &&
                        world.getBlockState(nextPos).getBlock() instanceof WisteriaLeavesBlock) {
                    next.add(nextPos);
                }
            }
        }

        super.randomTick(state, world, pos, random);
    }
}
