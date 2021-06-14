package com.aether.world.feature;

import com.aether.blocks.AetherBlocks;
import com.aether.world.feature.config.DynamicConfiguration;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.LightType;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class AetherLakeFeature extends Feature<DynamicConfiguration> {
    private static final BlockState CAVE_AIR;

    private static final Codec<DynamicConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(DynamicConfiguration::getState),
            Codec.STRING.optionalFieldOf("genType").forGetter(DynamicConfiguration::getGenString)
    ).apply(instance, DynamicConfiguration::new));

    static {
        CAVE_AIR = Blocks.CAVE_AIR.getDefaultState();
    }

    public AetherLakeFeature() {
        super(CODEC);
    }

    public boolean generate(FeatureContext<DynamicConfiguration> context) {
        BlockPos blockPos = context.getOrigin();
        while (blockPos.getY() > 60 && context.getWorld().isAir(blockPos)) {
            blockPos = blockPos.down();
        }

        if (blockPos.getY() <= 59) {
            return false;
        } else {
            blockPos = blockPos.down(4);
            if (context.getWorld().getStructures(ChunkSectionPos.from(blockPos), StructureFeature.VILLAGE).findAny().isPresent()) {
                return false;
            } else {
                boolean[] bls = new boolean[2048];
                int i = context.getRandom().nextInt(4) + 4;

                int ab;
                for (ab = 0; ab < i; ++ab) {
                    double d = context.getRandom().nextDouble() * 6.0D + 3.0D;
                    double e = context.getRandom().nextDouble() * 4.0D + 2.0D;
                    double f = context.getRandom().nextDouble() * 6.0D + 3.0D;
                    double g = context.getRandom().nextDouble() * (16.0D - d - 2.0D) + 1.0D + d / 2.0D;
                    double h = context.getRandom().nextDouble() * (8.0D - e - 4.0D) + 2.0D + e / 2.0D;
                    double k = context.getRandom().nextDouble() * (16.0D - f - 2.0D) + 1.0D + f / 2.0D;

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
                                Material material = context.getWorld().getBlockState(blockPos.add(ab, ad, ac)).getMaterial();
                                if (ad >= 4 && material.isLiquid()) {
                                    return false;
                                }

                                if (ad < 4 && !material.isSolid() && context.getWorld().getBlockState(blockPos.add(ab, ad, ac)) != context.getConfig().state) {
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
                                context.getWorld().setBlockState(blockPos.add(ab, ad, ac), ad >= 4 ? CAVE_AIR : context.getConfig().state, 2);
                            }
                        }
                    }
                }

                BlockPos blockPos3;
                for (ab = 0; ab < 16; ++ab) {
                    for (ac = 0; ac < 16; ++ac) {
                        for (ad = 4; ad < 8; ++ad) {
                            if (bls[(ab * 16 + ac) * 8 + ad]) {
                                blockPos3 = blockPos.add(ab, ad - 1, ac);
                                if (isSoil(context.getWorld().getBlockState(blockPos3)) && context.getWorld().getLightLevel(LightType.SKY, blockPos.add(ab, ad, ac)) > 0) {
                                    context.getWorld().setBlockState(blockPos3, AetherBlocks.AETHER_GRASS_BLOCK.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }

                if (context.getConfig().state.getMaterial() == Material.LAVA) {
                    for (ab = 0; ab < 16; ++ab) {
                        for (ac = 0; ac < 16; ++ac) {
                            for (ad = 0; ad < 8; ++ad) {
                                bl2 = !bls[(ab * 16 + ac) * 8 + ad] && (ab < 15 && bls[((ab + 1) * 16 + ac) * 8 + ad] || ab > 0 && bls[((ab - 1) * 16 + ac) * 8 + ad] || ac < 15 && bls[(ab * 16 + ac + 1) * 8 + ad] || ac > 0 && bls[(ab * 16 + (ac - 1)) * 8 + ad] || ad < 7 && bls[(ab * 16 + ac) * 8 + ad + 1] || ad > 0 && bls[(ab * 16 + ac) * 8 + (ad - 1)]);
                                if (bl2 && (ad < 4 || context.getRandom().nextInt(2) != 0) && context.getWorld().getBlockState(blockPos.add(ab, ad, ac)).getMaterial().isSolid()) {
                                    context.getWorld().setBlockState(blockPos.add(ab, ad, ac), AetherBlocks.HOLYSTONE.getDefaultState(), 2);
                                }
                            }
                        }
                    }
                }

                if (context.getConfig().state.getMaterial() == Material.WATER) {
                    for (ab = 0; ab < 16; ++ab) {
                        for (ac = 0; ac < 16; ++ac) {
                            blockPos3 = blockPos.add(ab, 4, ac);
                            if (context.getWorld().getBiome(blockPos3).canSetIce(context.getWorld(), blockPos3, false)) {
                                context.getWorld().setBlockState(blockPos3, Blocks.ICE.getDefaultState(), 2);
                            }
                        }
                    }
                }

                return true;
            }
        }
    }
}