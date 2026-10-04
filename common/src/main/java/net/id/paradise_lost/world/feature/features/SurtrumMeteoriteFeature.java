package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class SurtrumMeteoriteFeature extends Feature<NoneFeatureConfiguration> {

    public SurtrumMeteoriteFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos center = context.origin();
        WorldGenLevel world = context.level();

        RandomSource rand = context.random();
        final float size = 4;

        for (BlockPos i : BlockPos.withinManhattan(center, ((int) size) + 3, ((int) size) + 3, ((int) size) + 3)) {
            if (Math.sqrt(center.distSqr(i)) < size - 2f - rand.nextFloat() * 0.5) {
                world.setBlock(i, BlockRegistry.SURTRUM_AIR.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            } else if (Math.sqrt(center.distSqr(i)) < size - 1.5f + rand.nextFloat() * 0.5) {
                world.setBlock(i, BlockRegistry.SURTRUM.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            } else if (Math.sqrt(center.distSqr(i)) < size + rand.nextFloat() * 0.5) {
                world.setBlock(i, BlockRegistry.METAMORPHIC_SHELL.get().defaultBlockState(), Block.UPDATE_CLIENTS);
            } else if (Math.sqrt(center.distSqr(i)) < size + rand.nextFloat() * 0.7) {
                if (rand.nextBoolean()) {
                    for (BlockPos j : BlockPos.withinManhattan(i, 1, 1, 1)) {
                        if (Math.sqrt(i.distSqr(j)) < 1 + rand.nextFloat() && world.getBlockState(j).isAir()) world.setBlock(j, BlockRegistry.FLOESTONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                } else {
                    world.setBlock(i, BlockRegistry.FLOESTONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                }

            }
        }

        return true;
    }

}
