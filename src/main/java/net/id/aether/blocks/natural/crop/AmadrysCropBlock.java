package net.id.aether.blocks.natural.crop;

import net.id.aether.items.AetherItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.item.ItemConvertible;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.Random;

public class AmadrysCropBlock extends CropBlock {

    public AmadrysCropBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        final int[] waterSpots = {0};
        BlockPos.iterateOutwards(pos, 1, 1, 1).iterator().forEachRemaining(check -> {
            if(world.isWater(check)) {
                waterSpots[0]++;
            }
        });
        if(waterSpots[0] > 1) {
            super.randomTick(state, world, pos, random);
        }
    }

    @Override
    protected ItemConvertible getSeedsItem() {
        return AetherItems.AMADRYS_BUSHEL;
    }
}
