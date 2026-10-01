package net.id.paradise_lost.world.feature.tree.placers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

public class PointedBallFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<PointedBallFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            IntProvider.codec(0, 16).fieldOf("offset").forGetter(placer -> placer.offset),
            IntProvider.codec(1, 4).fieldOf("count").forGetter(placer -> placer.count)
    ).apply(instance, PointedBallFoliagePlacer::new));

    protected final IntProvider count;

    public PointedBallFoliagePlacer(IntProvider offset, IntProvider count) {
        super(offset, offset);
        this.count = count;
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ParadiseLostTreeHell.POINTED_BALL_FOLIAGE;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {

        BlockPos center = treeNode.pos().below(offset);

        for (int blob = 0; blob < this.count.sample(random); blob++) {
            BlockPos startBlock = center.below(blob * 4);
            if (blob == 0 && random.nextBoolean()) {
                generateSquare(world, placer, random, config, startBlock.above(1), 1, false);
                generateSquare(world, placer, random, config, startBlock.above(2), 1, true);
                generateSquare(world, placer, random, config, startBlock.above(3), 1, false);
                generateSquare(world, placer, random, config, startBlock.above(4), 0, true);
                generateSquare(world, placer, random, config, startBlock.above(5), 0, true);

            } else {
                generateSquare(world, placer, random, config, startBlock, 1, true);
                generateSquare(world, placer, random, config, startBlock.above(1), 2, false);
                generateSquare(world, placer, random, config, startBlock.above(2), 2, true);
                generateSquare(world, placer, random, config, startBlock.above(3), 2, false);
                generateSquare(world, placer, random, config, startBlock.above(4), 1, true);
                generateSquare(world, placer, random, config, startBlock.above(5), 0, true);
                generateSquare(world, placer, random, config, startBlock.above(6), 0, true);
            }

        }
    }

    private void generateSquare(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, BlockPos center, int size, boolean corners) {
        for (int x = -size; x <= size; x++) {
            for (int z = -size; z <= size; z++) {

                BlockPos iterPos = center.offset(x, 0, z);

                if ((corners || x == 0 || Math.abs(x) != Math.abs(z)) && (world.isStateAtPosition(iterPos, BlockBehaviour.BlockStateBase::isAir) || TreeFeature.validTreePos(world, iterPos))) {
                    BlockState leafBlock = config.foliageProvider.getState(random, iterPos);
                    placer.set(iterPos, leafBlock);
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int dx, int y, int dz, int radius, boolean giantTrunk) {
        return false;
    }
}
