package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import com.aether.world.feature.config.DynamicConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.Material;

public class AetherLakeFeature extends Feature<DynamicConfiguration> {
    private static final BlockState CAVE_AIR;

    private static final Codec<DynamicConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(DynamicConfiguration::getState),
            Codec.STRING.optionalFieldOf("genType").forGetter(DynamicConfiguration::getGenString)
    ).apply(instance, DynamicConfiguration::new));

    static {
        CAVE_AIR = Blocks.CAVE_AIR.defaultBlockState();
    }

    public AetherLakeFeature() {
        super(CODEC);
    }

    public boolean place(FeaturePlaceContext<DynamicConfiguration> context) {
        BlockPos blockPos = context.origin();
        while (blockPos.getY() > 60 && context.level().isEmptyBlock(blockPos)) {
            blockPos = blockPos.below();
        }

        if (blockPos.getY() <= 59) {
            return false;
        } else {
            blockPos = blockPos.below(4);
            if (context.level().startsForFeature(SectionPos.of(blockPos), StructureFeature.VILLAGE).findAny().isPresent()) {
                return false;
            } else {
                boolean[] bls = new boolean[2048];
                int i = context.random().nextInt(4) + 4;

                int ab;
                for (ab = 0; ab < i; ++ab) {
                    double d = context.random().nextDouble() * 6.0D + 3.0D;
                    double e = context.random().nextDouble() * 4.0D + 2.0D;
                    double f = context.random().nextDouble() * 6.0D + 3.0D;
                    double g = context.random().nextDouble() * (16.0D - d - 2.0D) + 1.0D + d / 2.0D;
                    double h = context.random().nextDouble() * (8.0D - e - 4.0D) + 2.0D + e / 2.0D;
                    double k = context.random().nextDouble() * (16.0D - f - 2.0D) + 1.0D + f / 2.0D;

                    for (int l = 1; l < 15; ++l) {
                        for (int m = 1; m < 15; ++m) {
                            for (int n = 1; n < 7; ++n) {
                                double o = ((double) l - g) / (d / 2.0D);
                                double p = ((double) n - h) / (e / 2.0D);
                                double q = ((double) m - k) / (f / 2.0D);
                                double r = o * o + p * p + q * q;
                                if (r < 1.0D) {
                                    bls[(l * 16 + m) * 8 + n] = true;
                                }
                            }
                        }
                    }
                }

                int ad;
                int ac;
                boolean bl2;
                for (ab = 0; ab < 16; ++ab) {
                    for (ac = 0; ac < 16; ++ac) {
                        for (ad = 0; ad < 8; ++ad) {
                            bl2 = !bls[(ab * 16 + ac) * 8 + ad] && (ab < 15 && bls[((ab + 1) * 16 + ac) * 8 + ad] || ab > 0 && bls[((ab - 1) * 16 + ac) * 8 + ad] || ac < 15 && bls[(ab * 16 + ac + 1) * 8 + ad] || ac > 0 && bls[(ab * 16 + (ac - 1)) * 8 + ad] || ad < 7 && bls[(ab * 16 + ac) * 8 + ad + 1] || ad > 0 && bls[(ab * 16 + ac) * 8 + (ad - 1)]);
                            if (bl2) {
                                Material material = context.level().getBlockState(blockPos.offset(ab, ad, ac)).getMaterial();
                                if (ad >= 4 && material.isLiquid()) {
                                    return false;
                                }

                                if (ad < 4 && !material.isSolid() && context.level().getBlockState(blockPos.offset(ab, ad, ac)) != context.config().state) {
                                    return false;
                                }
                            }
                        }
                    }
                }

                for (ab = 0; ab < 16; ++ab) {
                    for (ac = 0; ac < 16; ++ac) {
                        for (ad = 0; ad < 8; ++ad) {
                            if (bls[(ab * 16 + ac) * 8 + ad]) {
                                context.level().setBlock(blockPos.offset(ab, ad, ac), ad >= 4 ? CAVE_AIR : context.config().state, 2);
                            }
                        }
                    }
                }

                BlockPos blockPos3;
                for (ab = 0; ab < 16; ++ab) {
                    for (ac = 0; ac < 16; ++ac) {
                        for (ad = 4; ad < 8; ++ad) {
                            if (bls[(ab * 16 + ac) * 8 + ad]) {
                                blockPos3 = blockPos.offset(ab, ad - 1, ac);
                                if (isDirt(context.level().getBlockState(blockPos3)) && context.level().getBrightness(LightLayer.SKY, blockPos.offset(ab, ad, ac)) > 0) {
                                    context.level().setBlock(blockPos3, AetherBlocks.AETHER_GRASS_BLOCK.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }

                if (context.config().state.getMaterial() == Material.LAVA) {
                    for (ab = 0; ab < 16; ++ab) {
                        for (ac = 0; ac < 16; ++ac) {
                            for (ad = 0; ad < 8; ++ad) {
                                bl2 = !bls[(ab * 16 + ac) * 8 + ad] && (ab < 15 && bls[((ab + 1) * 16 + ac) * 8 + ad] || ab > 0 && bls[((ab - 1) * 16 + ac) * 8 + ad] || ac < 15 && bls[(ab * 16 + ac + 1) * 8 + ad] || ac > 0 && bls[(ab * 16 + (ac - 1)) * 8 + ad] || ad < 7 && bls[(ab * 16 + ac) * 8 + ad + 1] || ad > 0 && bls[(ab * 16 + ac) * 8 + (ad - 1)]);
                                if (bl2 && (ad < 4 || context.random().nextInt(2) != 0) && context.level().getBlockState(blockPos.offset(ab, ad, ac)).getMaterial().isSolid()) {
                                    context.level().setBlock(blockPos.offset(ab, ad, ac), AetherBlocks.HOLYSTONE.defaultBlockState(), 2);
                                }
                            }
                        }
                    }
                }

                if (context.config().state.getMaterial() == Material.WATER) {
                    for (ab = 0; ab < 16; ++ab) {
                        for (ac = 0; ac < 16; ++ac) {
                            blockPos3 = blockPos.offset(ab, 4, ac);
                            if (context.level().getBiome(blockPos3).shouldFreeze(context.level(), blockPos3, false)) {
                                context.level().setBlock(blockPos3, Blocks.ICE.defaultBlockState(), 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
    }
}