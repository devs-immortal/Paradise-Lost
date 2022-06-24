package net.id.aether.blocks.natural.crop;

import net.id.aether.items.AetherItems;
import net.id.aether.tag.AetherBlockTags;
import net.id.incubus_core.block.TallCropBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class FlaxCropBlock extends TallCropBlock {

    public FlaxCropBlock(AbstractBlock.Settings settings) {
        super(settings, 3);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (this.getHalf(state) == DoubleBlockHalf.UPPER) {
            return;
        }
        final int[] stoneSpots = {0};
        BlockPos.iterateOutwards(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            BlockState checkState = world.getBlockState(check);
            if(checkState.isIn(AetherBlockTags.BASE_AETHER_STONE) || checkState.isIn(BlockTags.BASE_STONE_OVERWORLD) || checkState.isOf(Blocks.GRAVEL)) {
                stoneSpots[0]++;
            }
        });
        if(stoneSpots[0] == 0) {
            tryGrow(state, world, pos, random, 40F);
        } else {
            tryGrow(state, world, pos, random, 14F + 16F / stoneSpots[0]);
        }
    }

    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return AetherItems.FLAXSEED;
    }

}
