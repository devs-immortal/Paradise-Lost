package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.world.feature.configs.DynamicConfiguration;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class ParadiseLostLakeFeature extends Feature<DynamicConfiguration> {
    private static final BlockState CAVE_AIR;

    static {
        CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    }
    
    public ParadiseLostLakeFeature(Codec<DynamicConfiguration> codec) {
        super(codec);
    }

    // FIXME 1.18.2-pre1
    @Override
    public boolean generate(FeatureContext<DynamicConfiguration> context) {
        BlockPos blockPos = context.getOrigin();

        if (blockPos.getY() <= 123 + context.getWorld().getBottomY()) {
            // Too low, didn't find a high enough block
            return false;
        } else {
            // Move the position to the bottom of the lake
            blockPos = blockPos.down(4);

            boolean[] waterMap = new boolean[2048];
            int lakeSize = context.getRandom().nextInt(4) + 4;

            // Generate water map
            for (int i = 0; i < lakeSize; i++) {
                double xSize = context.getRandom().nextDouble() * 6.0D + 3.0D;
                double ySize = context.getRandom().nextDouble() * 4.0D + 2.0D;
                double zSize = context.getRandom().nextDouble() * 6.0D + 3.0D;
                double xCenter = context.getRandom().nextDouble() * (16.0D - xSize - 2.0D) + 1.0D + xSize / 2.0D;
                double yCenter = context.getRandom().nextDouble() * (8.0D - ySize - 4.0D) + 2.0D + ySize / 2.0D;
                double zCenter = context.getRandom().nextDouble() * (16.0D - zSize - 2.0D) + 1.0D + zSize / 2.0D;

                for (int xOff = 1; xOff < 15; xOff++) {
                    for (int zOff = 1; zOff < 15; zOff++) {
                        for (int yOff = 1; yOff < 7; yOff++) {
                            double o = ((double) xOff - xCenter) / (xSize / 2.0D);
                            double p = ((double) yOff - yCenter) / (ySize / 2.0D);
                            double q = ((double) zOff - zCenter) / (zSize / 2.0D);
                            double r = o * o + p * p + q * q;
                            if (r < 1.0D) {
                                waterMap[(xOff * 16 + zOff) * 8 + yOff] = true;
                            }
                        }
                    }
                }
            }

            for (int xOff = 0; xOff < 16; xOff++) {
                for (int zOff = 0; zOff < 16; zOff++) {
                    for (int yOff = 0; yOff < 8; yOff++) {
                        //TODO Break this thing down some.
                        boolean lakeEdge =
                                !waterMap[(xOff * 16 + zOff) * 8 + yOff]
                                && (xOff < 15 && waterMap[((xOff + 1) * 16 + zOff) * 8 + yOff]
                                    || xOff > 0 && waterMap[((xOff - 1) * 16 + zOff) * 8 + yOff]
                                    || zOff < 15 && waterMap[(xOff * 16 + zOff + 1) * 8 + yOff]
                                    || zOff > 0 && waterMap[(xOff * 16 + (zOff - 1)) * 8 + yOff]
                                    || yOff < 7 && waterMap[(xOff * 16 + zOff) * 8 + yOff + 1]
                                    || yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]
                                );

                        if (lakeEdge) {
                            var state = context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff));

                            // There is a liquid above the lake, abort
                            if (yOff >= 4 && state.isLiquid()) {
                                return false;
                            }

                            // There is a non-solid, non-lake block on the edge, abort
                            if (yOff < 4 && !state.isSolid() && context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff)) != context.getConfig().state) {
                                return false;
                            }
                        }
                    }
                }
            }

            // Fill the lake with fluid, including air
            for (int xOff = 0; xOff < 16; xOff++) {
                for (int zOff = 0; zOff < 16; zOff++) {
                    for (int yOff = 0; yOff < 8; yOff++) {
                        if (waterMap[(xOff * 16 + zOff) * 8 + yOff]) {
                            context.getWorld().setBlockState(blockPos.add(xOff, yOff, zOff), yOff >= 4 ? CAVE_AIR : context.getConfig().state, Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }

            // Replace soil that has access to any skylight with grass
            for (int xOff = 0; xOff < 16; xOff++) {
                for (int zOff = 0; zOff < 16; zOff++) {
                    for (int yOff = 4; yOff < 8; yOff++) {
                        if (waterMap[(xOff * 16 + zOff) * 8 + yOff]) {
                            var blockPos3 = blockPos.add(xOff, yOff - 1, zOff);
                            if (isSoil(context.getWorld().getBlockState(blockPos3)) && context.getWorld().getLightLevel(LightType.SKY, blockPos.add(xOff, yOff, zOff)) > 0) {
                                context.getWorld().setBlockState(blockPos3, ParadiseLostBlocks.HIGHLANDS_GRASS.getDefaultState(), Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
            }


            // Replace 50% of lave lake edge blocks with floestone.
            if (context.getConfig().state.getFluidState().isIn(FluidTags.LAVA)) {
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
                                        || yOff > 0 && waterMap[(xOff * 16 + zOff) * 8 + (yOff - 1)]
                                    );
                            if (lakeEdge && (yOff < 4 || context.getRandom().nextInt(2) != 0) && context.getWorld().getBlockState(blockPos.add(xOff, yOff, zOff)).isSolid()) {
                                context.getWorld().setBlockState(blockPos.add(xOff, yOff, zOff), ParadiseLostBlocks.FLOESTONE.getDefaultState(), Block.NOTIFY_LISTENERS);
                            }
                        }
                    }
                }
            }

            // Cover water lakes with ice in frozen biomes
            if (context.getConfig().state.getFluidState().isIn(FluidTags.WATER)) {
                for (int xOff = 0; xOff < 16; xOff++) {
                    for (int zOff = 0; zOff < 16; zOff++) {
                        var blockPos3 = blockPos.add(xOff, 4, zOff);
                        if (context.getWorld().getBiome(blockPos3).value().canSetIce(context.getWorld(), blockPos3, false)) {
                            context.getWorld().setBlockState(blockPos3, Blocks.ICE.getDefaultState(), Block.NOTIFY_LISTENERS);
                        }
                    }
                }
            }

            return true;
        }
    }
}
