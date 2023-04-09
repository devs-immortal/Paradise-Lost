package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class SurtrumMeteoriteFeature extends Feature<DefaultFeatureConfig> {
    
    public SurtrumMeteoriteFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        BlockPos center = context.getOrigin();
        StructureWorldAccess world = context.getWorld();

        Random rand = context.getRandom();
        final float size = 4;

        for (BlockPos i : BlockPos.iterateOutwards(center, ((int) size)+3, ((int) size)+3, ((int) size)+3)) {
            if (Math.sqrt(center.getSquaredDistance(i)) < size-2f-rand.nextFloat()*0.5) {
                world.setBlockState(i, ParadiseLostBlocks.SURTRUM_AIR.getDefaultState(), Block.NOTIFY_LISTENERS);
            } else if (Math.sqrt(center.getSquaredDistance(i)) < size-1.5f+rand.nextFloat()*0.5) {
                world.setBlockState(i, ParadiseLostBlocks.SURTRUM.getDefaultState(), Block.NOTIFY_LISTENERS);
            } else if (Math.sqrt(center.getSquaredDistance(i)) < size+rand.nextFloat()*0.5) {
                world.setBlockState(i, ParadiseLostBlocks.METAMORPHIC_SHELL.getDefaultState(), Block.NOTIFY_LISTENERS);
            } else if (Math.sqrt(center.getSquaredDistance(i)) < size+rand.nextFloat()*0.7) {
                if (rand.nextBoolean()) {
                    for (BlockPos j : BlockPos.iterateOutwards(i, 1, 1, 1)) {
                        if (Math.sqrt(i.getSquaredDistance(j)) < 1+rand.nextFloat() && world.getBlockState(j).isAir()) world.setBlockState(j, ParadiseLostBlocks.FLOESTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                    }
                } else {
                    world.setBlockState(i, ParadiseLostBlocks.FLOESTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

            }
        }

        return true;
    }
    
}
