package net.id.aether.blocks.natural.crop;

import net.id.aether.blocks.natural.TallCropBlock;
import net.id.aether.items.AetherItems;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FlaxCropBlock extends TallCropBlock {

    public FlaxCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if (this.getHalf(state) == DoubleBlockHalf.UPPER) {
            return;
        }
        final int[] stoneSpots = {0};
        BlockPos.iterateOutwards(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            Block block = world.getBlockState(pos).getBlock();
            if(AetherBlockTags.BASE_AETHER_STONE.contains(block) || BlockTags.BASE_STONE_OVERWORLD.contains(block) || block.equals(Blocks.GRAVEL)) {
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
