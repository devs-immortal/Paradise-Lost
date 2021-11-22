package net.id.aether.world.feature.tree.placers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.aether.util.AStarManager;
import net.id.aether.world.feature.tree.AetherTreeHell;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.floatprovider.FloatProvider;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.WorldView;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class WisteriaTrunkPlacer extends TrunkPlacer {

    public static final Codec<WisteriaTrunkPlacer> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("tries").forGetter(placer -> placer.tries),
            IntProvider.VALUE_CODEC.fieldOf("xOffset").forGetter(placer -> placer.xOffset),
            IntProvider.VALUE_CODEC.fieldOf("yOffset").forGetter(placer -> placer.yOffset),
            IntProvider.VALUE_CODEC.fieldOf("zOffset").forGetter(placer -> placer.zOffset),
            FloatProvider.VALUE_CODEC.fieldOf("secondaryGrowthChance").forGetter(placer -> placer.secondaryGrowthChance),
            Codec.INT.fieldOf("base_height").forGetter(placer -> placer.baseHeight),
            Codec.INT.fieldOf("height_rand_a").forGetter(placer -> placer.firstRandomHeight),
            Codec.INT.fieldOf("height_rand_b").forGetter(placer -> placer.secondRandomHeight)
            ).apply(instance, WisteriaTrunkPlacer::new));

    private final IntProvider tries;
    private final IntProvider xOffset, yOffset, zOffset;
    private final FloatProvider secondaryGrowthChance;

    public WisteriaTrunkPlacer(IntProvider tries, IntProvider xOffset, IntProvider yOffset, IntProvider zOffset, FloatProvider secondaryGrowthChance, int baseHeight, int firstRandomHeight, int secondRandomHeight) {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
        this.tries = tries;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
        this.secondaryGrowthChance = secondaryGrowthChance;
    }

    @Override
    protected TrunkPlacerType<?> getType() {
        return AetherTreeHell.WISTERIA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        List<FoliagePlacer.TreeNode> nodes = new ArrayList<>();

        int firstHeight = random.nextInt(baseHeight) + baseHeight / 2 + 1;

        for (int i = 0; i < firstHeight; i++) {
            getAndSetState(world, replacer, random, startPos.up(i), config);
        }

        if(firstHeight > 4) {
            for (Direction direction : Direction.values()) {
                if(random.nextBoolean()) {
                    var sideHeight = random.nextInt(firstHeight + 1) / 2;
                    var sidePos = startPos.offset(direction);
                    for (int i = 0; i <= sideHeight; i++) {
                        getAndSetState(world, replacer, random, sidePos.up(i), config);
                    }
                }
            }
        }

        var firstPos = startPos.up(firstHeight).mutableCopy();

        var builder = AStarManager.createBuilder();
        builder.start(AStarManager.BlockPosProvider.simple(firstPos));
        builder.goal(worldview -> {
            var xOff = xOffset.get(random) * (random.nextBoolean() ? -1 : 1);
            var yOff = yOffset.get(random);
            var zOff = zOffset.get(random) * (random.nextBoolean() ? -1 : 1);
            return firstPos.add(xOff, yOff, zOff);
        });
        //builder.costMapper(AStarManager.Builder.c_favorReplaceable);
        builder.allowRecompute();
        //builder.allowDiagonalMovement();
        var pathfinder = builder.build((WorldView) world);

        var branches = tries.get(random);
        var secondaryGrowth = random.nextFloat() <= secondaryGrowthChance.get(random);

        if(random.nextBoolean()) {
            nodes.add(new FoliagePlacer.TreeNode(firstPos, 0, false));
        }

        for (int i = 0; i < branches / (secondaryGrowth ? 2 : 1); i++) {
            pathfinder.compute();
            pathfinder.getLastOutput().ifPresent(pathingOutput -> {

                nodes.add(new FoliagePlacer.TreeNode(pathingOutput.path().get(0).pos(), 0, false));

                for (AStarManager.Node node : pathingOutput.path()) {
                    getAndSetState(world, replacer, random, node.pos(), config);
                }
            });
        }

        if(secondaryGrowth) {
            int secHeight = firstHeight / 3 + random.nextInt(secondRandomHeight);

            for (int i = 0; i < secHeight; i++) {
                getAndSetState(world, replacer, random, firstPos, config);
                firstPos.move(Direction.UP);
            }

            for (int i = 0; i < branches; i++) {
                pathfinder.compute();
                pathfinder.getLastOutput().ifPresent(pathingOutput -> {

                    nodes.add(new FoliagePlacer.TreeNode(pathingOutput.path().get(0).pos(), 0, false));

                    for (AStarManager.Node node : pathingOutput.path()) {
                        getAndSetState(world, replacer, random, node.pos(), config);
                    }
                });
            }
        }

        return nodes;
    }
}
