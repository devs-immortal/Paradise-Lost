package net.id.paradise_lost.block.decorative;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirtPathBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ParadiseLostDirtPathBlock extends DirtPathBlock {

    private Supplier<Block> returnTo;

    public ParadiseLostDirtPathBlock(Properties settings, Supplier<Block> returnTo) {
        super(settings);
        this.returnTo = returnTo;
    }

    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, RandomSource random) {
        world.setBlockAndUpdate(pos, pushEntitiesUp(state, this.returnTo.get().defaultBlockState(), world, pos));
    }
}
