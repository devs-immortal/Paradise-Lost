package net.id.paradiselost.world.feature.tree.placers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

public class PointedBallFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<PointedBallFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            IntProvider.createValidatingCodec(0, 16).fieldOf("offset").forGetter(placer -> placer.offset),
            IntProvider.createValidatingCodec(1, 4).fieldOf("count").forGetter(placer -> placer.count)
    ).apply(instance, PointedBallFoliagePlacer::new));

    protected final IntProvider count;

    public PointedBallFoliagePlacer(IntProvider offset, IntProvider count) {
        super(offset, offset);
        this.count = count;
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ParadiseLostTreeHell.POINTED_BALL_FOLIAGE;
    }

    @Override
    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {

        BlockPos center = treeNode.getCenter().down(offset);

        for (int blob = 0; blob < this.count.get(random); blob++) {
            BlockPos startBlock = center.down(blob * 4);
            if (blob == 0 && random.nextBoolean()) {
                generateSquare(world, placer, random, config, startBlock.up(1), 1, false);
                generateSquare(world, placer, random, config, startBlock.up(2), 1, true);
                generateSquare(world, placer, random, config, startBlock.up(3), 1, false);
                generateSquare(world, placer, random, config, startBlock.up(4), 0, true);
                generateSquare(world, placer, random, config, startBlock.up(5), 0, true);

            } else {
                generateSquare(world, placer, random, config, startBlock, 1, true);
                generateSquare(world, placer, random, config, startBlock.up(1), 2, false);
                generateSquare(world, placer, random, config, startBlock.up(2), 2, true);
                generateSquare(world, placer, random, config, startBlock.up(3), 2, false);
                generateSquare(world, placer, random, config, startBlock.up(4), 1, true);
                generateSquare(world, placer, random, config, startBlock.up(5), 0, true);
                generateSquare(world, placer, random, config, startBlock.up(6), 0, true);
            }

        }
    }

    private void generateSquare(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, BlockPos center, int size, boolean corners) {
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {
                // block pos we are setting
                BlockPos iterPos = center.add(x, 0, z);
                // if placeable, place
                if ((corners || x == 0 || Math.abs(x) != Math.abs(z)) && (world.testBlockState(iterPos, AbstractBlock.AbstractBlockState::isAir) || TreeFeature.canReplace(world, iterPos))) {
                    BlockState leafBlock = config.foliageProvider.get(random, iterPos);
                    placer.placeBlock(iterPos, leafBlock);
                }
            }
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return false;
    }
}
