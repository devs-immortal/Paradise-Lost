package net.id.paradiselost.world.feature.tree.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class WisteriaTrunkPlacer extends TrunkPlacer {

    /*
     * Actual function used for branches
     * https://www.desmos.com/calculator/n4q9yugst4
     */

    public static final Codec<WisteriaTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("maxBranchRange").forGetter(placer -> placer.maxBranchRange),
            IntProvider.VALUE_CODEC.fieldOf("branchCount").forGetter(placer -> placer.branchCount),
            FloatProvider.VALUE_CODEC.fieldOf("branchRange").forGetter(placer -> placer.branchRange),
            FloatProvider.VALUE_CODEC.fieldOf("branchHeight").forGetter(placer -> placer.branchHeight),
            Codec.INT.fieldOf("base_height").forGetter(placer -> placer.baseHeight),
            Codec.INT.fieldOf("height_rand_a").forGetter(placer -> placer.firstRandomHeight),
            Codec.INT.fieldOf("height_rand_b").forGetter(placer -> placer.secondRandomHeight)
    ).apply(instance, WisteriaTrunkPlacer::new));

    private final IntProvider maxBranchRange, branchCount;
    private final FloatProvider branchHeight, branchRange;

    public WisteriaTrunkPlacer(IntProvider maxBranchRange, IntProvider branchCount, FloatProvider branchRange, FloatProvider branchHeight, int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.maxBranchRange = maxBranchRange; // Furthest a branch can go from the tree
        this.branchCount = branchCount; // Amount of branches to generate
        this.branchHeight = branchHeight; // Variable height of the branches
        this.branchRange = branchRange; // Variable range of branches
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return ParadiseLostTreeHell.WISTERIA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        List<FoliagePlacer.TreeNode> nodes = new ArrayList<>();

        int firstHeight = random.nextInt(baseHeight) + baseHeight / 2 + 1;

        // Create initial trunk
        for (int i = 0; i <= firstHeight+1; i++) {
            getAndSetState(world, replacer, random, startPos.up(i), config);
        }
        // Generate branches from slightly lower than the top
        BlockPos trunkTop = startPos.up(firstHeight-2);
        // Put leaf node on top of trunk
        nodes.add(new FoliagePlacer.TreeNode(trunkTop.up(3), 0, false));

        int offset, previous;
        float a, b;
        Direction dir, dir2;
        int yOffset = 0;
        // For each branch
        for (int i = 0; i < branchCount.get(random); i++) {
            offset = 1;
            previous = 0;
            // Get random height and range
            a = branchHeight.get(random);
            b = branchRange.get(random);
            // Get random direction to send branch
            dir = randomDirection(random);
            // Get random offset to make it not a straight branch
            dir2 = random.nextBoolean() ? dir.rotateYClockwise() : dir.rotateYCounterclockwise();
            // While not at the cap
            while (offset <= maxBranchRange.getMax()) {
                yOffset = trunkFunc(offset, a, b);
                if (yOffset < 1) {
                    break;
                }
                if (previous == yOffset) { // If not moving upwards, don't fill
                    getAndSetState(world, replacer, random, trunkTop.up(yOffset).offset(dir, offset).offset(dir2, offset/2), config);
                } else { // But if we are, fill up to the point so everything is connected
                    for (int y = previous+1; y <= yOffset; y++) {
                        getAndSetState(world, replacer, random, trunkTop.up(y).offset(dir, offset).offset(dir2, offset/2), config);
                    }
                }
                offset++; // Move to next blockpos
                previous = yOffset; // Save offset for above position check
            }
            trunkTop = trunkTop.up(); // At end of branch, add leaf node
            nodes.add(new FoliagePlacer.TreeNode(trunkTop.up(previous-1).offset(dir, offset-2).offset(dir2, offset/4), 0, false));
        }

        return nodes;
    }

    // Curve function for generating a branch
    private int trunkFunc(float x, float a, float b) {
        return (int) Math.ceil(-Math.log((2*a / x) - b) + 3);
    }

    // Mojang, why isn't there a builtin function to do this?
    private static Direction[] directions = new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST};
    private Direction randomDirection(Random random) {
        return directions[random.nextInt(directions.length)];
    }

}
