package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class ParadiseLostLakeFeature extends Feature<BlockStateConfiguration> {
    private static final BlockState CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();

    public ParadiseLostLakeFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> context) {
        BlockPos blockPos = context.origin();

        if (blockPos.getY() <= 123 + context.level().getMinY()) {
            return false;
        }

        blockPos = blockPos.below(4);
        BlockState fluid = context.config().state;

        boolean[] waterMap = new boolean[2048];
        int lakeSize = context.random().nextInt(4) + 4;

        for (int i = 0; i < lakeSize; i++) {
            double xSize = context.random().nextDouble() * 6.0D + 3.0D;
            double ySize = context.random().nextDouble() * 4.0D + 2.0D;
            double zSize = context.random().nextDouble() * 6.0D + 3.0D;
            double xCenter = context.random().nextDouble() * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D;
            double yCenter = context.random().nextDouble() * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D;
            double zCenter = context.random().nextDouble() * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D;

            for (int xOff = 1; xOff < 15; xOff++) {
                for (int zOff = 1; zOff < 15; zOff++) {
                    for (int yOff = 1; yOff < 7; yOff++) {
                        double o = ((double) xOff - xCenter) / (xSize / 2.0D);
                        double p = ((double) yOff - yCenter) / (ySize / 2.0D);
                        double q = ((double) zOff - zCenter) / (zSize / 2.0D);
                        if (o * o + p * p + q * q < 1.0D) {
                            waterMap[(xOff * 16 + zOff) * 8 + yOff] = true;
                        }
                    }
                }
            }
        }

        for (int xOff = 0; xOff < 16; xOff++) {
            for (int zOff = 0; zOff < 16; zOff++) {
                for (int yOff = 0; yOff < 8; yOff++) {
                    boolean lakeEdge =
                            !waterMap[(xOff * 16 + zOff) * 8 + yOff]
                            && (xOff < 15 && waterMap[((xOff + 1) * 16 + zOff) * 8 + yOff]
                                || xOff > 0 && waterMap[((xOff - 1) * 16 + zOff) * 8 + yOff]
                                || zOff < 15 && waterMap[(xOff * 16 + zOff + 1) * 8 + yOff]
                                || zOff > 0 && waterMap[(xOff * 16 + (zOff - 1)) * 8 + yOff]
                                || yOff < 7 && waterMap[(xOff * 16 + zOff) * 8 + yOff + 1]
                                || yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]);

                    if (lakeEdge) {
                        var state = context.level().getBlockState(blockPos.offset(xOff, yOff, zOff));
                        if (yOff >= 4 && state.liquid()) {
                            return false;
                        }
                        if (yOff < 4 && !state.isSolid() && context.level().getBlockState(blockPos.offset(xOff, yOff, zOff)) != fluid) {
                            return false;
                        }
                    }
                }
            }
        }

        for (int xOff = 0; xOff < 16; xOff++) {
            for (int zOff = 0; zOff < 16; zOff++) {
                for (int yOff = 0; yOff < 8; yOff++) {
                    if (waterMap[(xOff * 16 + zOff) * 8 + yOff]) {
                        context.level().setBlock(blockPos.offset(xOff, yOff, zOff), yOff >= 4 ? CAVE_AIR : fluid, Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        for (int xOff = 0; xOff < 16; xOff++) {
            for (int zOff = 0; zOff < 16; zOff++) {
                for (int yOff = 4; yOff < 8; yOff++) {
                    if (waterMap[(xOff * 16 + zOff) * 8 + yOff]) {
                        var below = blockPos.offset(xOff, yOff - 1, zOff);
                        if (isDirt(context.level().getBlockState(below)) && context.level().getBrightness(LightLayer.SKY, blockPos.offset(xOff, yOff, zOff)) > 0) {
                            context.level().setBlock(below, BlockRegistry.HIGHLANDS_GRASS.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }

        if (fluid.getFluidState().is(FluidTags.LAVA)) {
            for (int xOff = 0; xOff < 16; xOff++) {
                for (int zOff = 0; zOff < 16; zOff++) {
                    for (int yOff = 0; yOff < 8; yOff++) {
                        boolean lakeEdge =
                                !waterMap[(xOff * 16 + zOff) * 8 + yOff]
                                && (xOff < 15 && waterMap[((xOff + 1) * 16 + zOff) * 8 + yOff]
                                    || xOff > 0 && waterMap[((xOff - 1) * 16 + zOff) * 8 + yOff]
                                    || zOff < 15 && waterMap[(xOff * 16 + zOff + 1) * 8 + yOff]
                                    || zOff > 0 && waterMap[(xOff * 16 + (zOff - 1)) * 8 + yOff]
                                    || yOff < 7 && waterMap[(xOff * 16 + zOff) * 8 + yOff + 1]
                                    || yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]);
                        if (lakeEdge && (yOff < 4 || context.random().nextInt(2) != 0) && context.level().getBlockState(blockPos.offset(xOff, yOff, zOff)).isSolid()) {
                            context.level().setBlock(blockPos.offset(xOff, yOff, zOff), BlockRegistry.FLOESTONE.get().defaultBlockState(), Block.UPDATE_CLIENTS);
                        }
                    }
                }
            }
        }

        if (fluid.getFluidState().is(FluidTags.WATER)) {
            for (int xOff = 0; xOff < 16; xOff++) {
                for (int zOff = 0; zOff < 16; zOff++) {
                    var surface = blockPos.offset(xOff, 4, zOff);
                    if (context.level().getBiome(surface).value().shouldFreeze(context.level(), surface, false)) {
                        context.level().setBlock(surface, Blocks.ICE.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
        }

        return true;
    }
}
