package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import net.id.aether.Aether;
import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.ProbabilityConfig;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class HoneyNettleFeature extends Feature<DefaultFeatureConfig> {

    public HoneyNettleFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        var world = context.getWorld();
        var config = context.getConfig();
        var origin = context.getOrigin();
        var random = context.getRandom();

        int i = random.nextInt(8) - random.nextInt(8);
        int j = random.nextInt(8) - random.nextInt(8);
        int k = world.getTopY(Heightmap.Type.OCEAN_FLOOR, origin.getX() + i, origin.getZ() + j);

        var adjustedPos = origin.withY(k);

        if(world.getBlockState(adjustedPos).isOf(Blocks.WATER) && world.getBlockState(adjustedPos.up()).isAir()) {
            world.setBlockState(adjustedPos, AetherBlocks.HONEY_NETTLE.getDefaultState().with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER).with(Properties.WATERLOGGED, true), 3);
            world.setBlockState(adjustedPos.up(), AetherBlocks.HONEY_NETTLE.getDefaultState().with(Properties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER).with(Properties.WATERLOGGED, false), 3);

            return true;
        }

        return false;
    }
}
