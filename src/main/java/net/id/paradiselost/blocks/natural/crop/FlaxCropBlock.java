package net.id.paradiselost.blocks.natural.crop;

import net.id.incubus_core.block.TallCropBlock;
import net.id.paradiselost.items.ParadiseLostItems;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;

public class FlaxCropBlock extends TallCropBlock {
    
    public FlaxCropBlock(Settings settings) {
        super(settings, 2);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (this.getHalf(state) == DoubleBlockHalf.UPPER) {
            return;
        }
        var data = new Object() {
            int stoneSpots = 0;
        };
        BlockPos.iterateOutwards(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            BlockState checkState = world.getBlockState(check);
            if (checkState.isIn(ParadiseLostBlockTags.BASE_PARADISE_LOST_STONE) || checkState.isIn(BlockTags.BASE_STONE_OVERWORLD) || checkState.isOf(Blocks.GRAVEL)) {
                data.stoneSpots++;
            }
        });
        if (data.stoneSpots == 0) {
            tryGrow(state, world, pos, random, 20F);
        } else {
            tryGrow(state, world, pos, random, 7F + 16F / data.stoneSpots);
        }
    }

    @Override
    public int getMaxAge() {
        return 7;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return ParadiseLostItems.FLAXSEED;
    }

}
