package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class HoneyNettleFeature extends Feature<NoneFeatureConfiguration> {

    public HoneyNettleFeature(Codec<NoneFeatureConfiguration> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        var world = context.level();
        var config = context.config();
        var origin = context.origin();
        var random = context.random();

        int i = random.nextInt(8) - random.nextInt(8);
        int j = random.nextInt(8) - random.nextInt(8);
        int k = world.getHeight(Heightmap.Types.OCEAN_FLOOR, origin.getX() + i, origin.getZ() + j);

        var adjustedPos = origin.atY(k);

        if (world.getBlockState(adjustedPos).is(Blocks.WATER) && world.getBlockState(adjustedPos.above()).isAir()) {
            world.setBlock(adjustedPos, BlockRegistry.HONEY_NETTLE.get().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).setValue(BlockStateProperties.WATERLOGGED, true), Block.UPDATE_ALL);
            world.setBlock(adjustedPos.above(), BlockRegistry.HONEY_NETTLE.get().defaultBlockState().setValue(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).setValue(BlockStateProperties.WATERLOGGED, false), Block.UPDATE_ALL);

            return true;
        }

        return false;
    }
}
