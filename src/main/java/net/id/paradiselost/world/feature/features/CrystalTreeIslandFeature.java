package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostLeavesBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class CrystalTreeIslandFeature extends Feature<DefaultFeatureConfig> {
    public CrystalTreeIslandFeature(Codec<DefaultFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DefaultFeatureConfig> context) {
        int checkSize = 4;
        for (int x = -checkSize; x >= checkSize; x++) {
            for (int y = -5; y >= 8; y++) {
                for (int z = -checkSize; z >= checkSize; z++) {
                    if (!context.getWorld().getBlockState(context.getOrigin().add(x, y, z)).isOf(Blocks.AIR)) {
                        return false;
                    }
                }
            }
        }

        float f = (float) (context.getRandom().nextInt(3) + 3);

        for (int y = 0; f > 0.5F; --y) {
            for (int x = MathHelper.floor(-f); x <= MathHelper.ceil(f); ++x) {
                for (int z = MathHelper.floor(-f); z <= MathHelper.ceil(f); ++z) {
                    if ((float) (x * x + z * z) <= (f + 1.0F) * (f + 1.0F)) {
                        if (y == 0) {
                            this.setBlockState(context.getWorld(), context.getOrigin().add(x, y, z), ParadiseLostBlocks.HIGHLANDS_GRASS.getDefaultState());
                            if (context.getRandom().nextInt(6) == 0) {
                                this.setBlockState(context.getWorld(), context.getOrigin().add(x, y + 1, z), ParadiseLostBlocks.GRASS.getDefaultState());
                            }
                        } else if (y == -1) {
                            this.setBlockState(context.getWorld(), context.getOrigin().add(x, y, z), ParadiseLostBlocks.DIRT.getDefaultState());
                        } else if (y == -2) {
                            if (context.getRandom().nextBoolean()) {
                                this.setBlockState(context.getWorld(), context.getOrigin().add(x, y, z), ParadiseLostBlocks.DIRT.getDefaultState());
                            } else {
                                this.setBlockState(context.getWorld(), context.getOrigin().add(x, y, z), ParadiseLostBlocks.HOLYSTONE.getDefaultState());
                            }
                        } else {
                            this.setBlockState(context.getWorld(), context.getOrigin().add(x, y, z), ParadiseLostBlocks.HOLYSTONE.getDefaultState());
                        }

                    }
                }
            }
            f = (float) ((double) f - ((double) context.getRandom().nextInt(2) + 0.5D));
        }
        int[] leafRadii = new int[]{0, 3, 2, 1, 2, 1, 0, 1};

        for (int y = 1; y < 9; y++) {
            this.generateTreeCircle(context.getWorld(), context.getRandom(), context.getOrigin().up(y), leafRadii[y - 1], ParadiseLostBlocks.CRYSTAL_LEAVES.getDefaultState().with(ParadiseLostLeavesBlock.DISTANCE, 1));
            this.setBlockState(context.getWorld(), context.getOrigin().up(y), ParadiseLostBlocks.CRYSTAL_LOG.getDefaultState());
        }

        this.setBlockState(context.getWorld(), context.getOrigin().up(9), ParadiseLostBlocks.CRYSTAL_LEAVES.getDefaultState().with(ParadiseLostLeavesBlock.DISTANCE, 1));

        return true;
    }

    private void generateTreeCircle(StructureWorldAccess structureWorldAccess, Random random, BlockPos blockPos, int radius, BlockState state) {
        if (radius < 1) {
            return;
        }
        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                if (!(Math.abs(x) == radius && Math.abs(z) == radius) || (x == 0 && z == 0)) {
                    if (structureWorldAccess.getBlockState(blockPos.add(x, 0, z)).isOf(Blocks.AIR)) {
                        this.setBlockState(structureWorldAccess, blockPos.add(x, 0, z), ParadiseLostBlocks.CRYSTAL_LEAVES.getDefaultState().with(ParadiseLostLeavesBlock.DISTANCE, 1));
                    }
                }
            }
        }
    }
}
