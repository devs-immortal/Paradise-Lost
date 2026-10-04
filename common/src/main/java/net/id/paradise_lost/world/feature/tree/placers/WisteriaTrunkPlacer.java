package net.id.paradise_lost.world.feature.tree.placers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class WisteriaTrunkPlacer extends TrunkPlacer {

    public static final MapCodec<WisteriaTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IntProvider.CODEC.fieldOf("max_branch_range").forGetter(placer -> placer.maxBranchRange),
            IntProvider.CODEC.fieldOf("branch_count").forGetter(placer -> placer.branchCount),
            FloatProvider.CODEC.fieldOf("branch_range").forGetter(placer -> placer.branchRange),
            FloatProvider.CODEC.fieldOf("branch_height").forGetter(placer -> placer.branchHeight)
    ).and(TrunkPlacer.trunkPlacerParts(instance)).apply(instance, WisteriaTrunkPlacer::new));

    private final IntProvider maxBranchRange, branchCount;
    private final FloatProvider branchHeight, branchRange;

    public WisteriaTrunkPlacer(IntProvider maxBranchRange, IntProvider branchCount, FloatProvider branchRange, FloatProvider branchHeight, int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.maxBranchRange = maxBranchRange;
        this.branchCount = branchCount;
        this.branchHeight = branchHeight;
        this.branchRange = branchRange;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ParadiseLostTreeHell.WISTERIA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        List<FoliagePlacer.FoliageAttachment> nodes = new ArrayList<>();

        int firstHeight = random.nextInt(baseHeight) + baseHeight / 2 + 1;

        for (int i = 0; i <= firstHeight + 1; i++) {
            placeLog(world, replacer, random, startPos.above(i), config);
        }

        BlockPos trunkTop = startPos.above(firstHeight - 2);

        nodes.add(new FoliagePlacer.FoliageAttachment(trunkTop.above(3), 0, false));

        int offset, previous;
        float a, b;
        Direction dir, dir2;
        int yOffset = 0;

        for (int i = 0; i < branchCount.sample(random); i++) {
            offset = 1;
            previous = 0;

            a = branchHeight.sample(random);
            b = branchRange.sample(random);

            dir = randomDirection(random);

            dir2 = random.nextBoolean() ? dir.getClockWise() : dir.getCounterClockWise();

            while (offset <= maxBranchRange.getMaxValue()) {
                yOffset = trunkFunc(offset, a, b);
                if (yOffset < 1) {
                    break;
                }
                if (previous == yOffset) {
                    placeLog(world, replacer, random, trunkTop.above(yOffset).relative(dir, offset).relative(dir2, offset / 2), config);
                } else {
                    for (int y = previous + 1; y <= yOffset; y++) {
                        placeLog(world, replacer, random, trunkTop.above(y).relative(dir, offset).relative(dir2, offset / 2), config);
                    }
                }
                offset++;
                previous = yOffset;
            }
            trunkTop = trunkTop.above();
            nodes.add(new FoliagePlacer.FoliageAttachment(trunkTop.above(previous - 1).relative(dir, offset - 2).relative(dir2, offset / 4), 0, false));
        }

        return nodes;
    }

    private int trunkFunc(float x, float a, float b) {
        return (int) Math.ceil(-Math.log((2 * a / x) - b) + 3);
    }

    private static final Direction[] directions = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private Direction randomDirection(RandomSource random) {
        return directions[random.nextInt(directions.length)];
    }

}
