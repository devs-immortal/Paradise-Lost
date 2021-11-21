package net.id.aether.blocks.natural.crop;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class SwetrootCropBlock extends CropBlock {

    public SwetrootCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        final int[] swetrootSpots = {0};
        BlockPos.iterateOutwards(pos, 1, 0, 1).iterator().forEachRemaining(check -> {
            Block block = world.getBlockState(pos).getBlock();
            if(block.equals(AetherBlocks.SWETROOT)) {
                swetrootSpots[0]++;
            }
        });
        int upperBound = 25;
        if(swetrootSpots[0] < 4) {
            upperBound = 15;
        }
        if (world.getLightLevel(pos) <= 5) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getAvailableMoisture(this, world, pos);
                if (random.nextInt((int)(upperBound / f) + 1) == 0) {
                    world.setBlockState(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }

    public int getMaxAge() {
        return 3;
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return AetherItems.SWETROOT;
    }
}
