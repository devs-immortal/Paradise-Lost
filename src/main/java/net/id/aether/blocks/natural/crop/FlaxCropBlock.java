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
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class FlaxCropBlock extends TallCropBlock {

    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

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
        if(stoneSpots[0] < 2) {
            tryGrow(state, world, pos, random, 28F);
        } else {
            tryGrow(state, world, pos, random, 20F);
        }
    }

    public int getMaxAge() {
        return 7;
    }

//    @Override
//    protected ItemConvertible getSeedsItem() {
//        return AetherItems.FLAXSEED;
//    }

}
