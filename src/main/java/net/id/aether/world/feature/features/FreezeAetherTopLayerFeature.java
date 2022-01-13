package net.id.aether.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.aether.blocks.natural.aercloud.AercloudBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class FreezeAetherTopLayerFeature extends Feature<DefaultFeatureConfig> {

    public FreezeAetherTopLayerFeature(Codec<DefaultFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        var world = context.getWorld();
        BlockPos origin = context.getOrigin();
        BlockPos.Mutable surface = new BlockPos.Mutable();
        BlockPos.Mutable floor = new BlockPos.Mutable();

        for(int i = 0; i < 16; ++i) {
            for(int j = 0; j < 16; ++j) {

                int x = origin.getX() + i;
                int z = origin.getZ() + j;
                int y = world.getTopY(Heightmap.Type.MOTION_BLOCKING, x, z);

                surface.set(x, y, z);

                var testState = world.getBlockState(surface);

                if(testState.getBlock() instanceof AercloudBlock) {

                    var testPos = surface.mutableCopy();

                    while (testPos.getY() > 0 && ((testState.getBlock() instanceof AercloudBlock || testState.isAir()) && !world.getBlockState(testPos.down()).isOpaque())) {
                        testPos.move(Direction.DOWN);
                    }
                }

                floor.set(surface).move(Direction.DOWN, 1);

                Biome biome = world.getBiome(surface);
                if (biome.canSetIce(world, floor, false)) {
                    world.setBlockState(floor, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                }

                if (biome.canSetSnow(world, surface)) {
                    world.setBlockState(surface, Blocks.SNOW.getDefaultState(), Block.NOTIFY_LISTENERS);
                    BlockState blockState = world.getBlockState(floor);
                    if (blockState.contains(SnowyBlock.SNOWY)) {
                        world.setBlockState(floor, blockState.with(SnowyBlock.SNOWY, true), Block.NOTIFY_LISTENERS);
                    }
                }
            }
        }

        return true;
    }
}
