package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.util.AStarManager;
import net.id.paradiselost.world.feature.configs.ProjectedOrganicCoverConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.HashSet;

public class ProjectedOrganicCoverFeature extends Feature<ProjectedOrganicCoverConfig> {

    public ProjectedOrganicCoverFeature(Codec<ProjectedOrganicCoverConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<ProjectedOrganicCoverConfig> context) {
        var world = context.getWorld();
        var random = context.getRandom();
        var origin = context.getOrigin();
        var config = context.getConfig();

        var lengthProvider = config.branchLength();
        var tries = config.tries().get(random);

        var builder = AStarManager.createBuilder();
        builder.heuristic(config.heuristic());
        builder.expectedLength(128);
        builder.start(AStarManager.BlockPosProvider.simple(origin));
        builder.goal(access -> {
            var length = lengthProvider.get(random) + 1;
            return origin.add(random.nextInt(length * 2) - length, 0, random.nextInt(length * 2) - length);
        });
        builder.validator((worldView, blockPos) -> blockPos.getY() == origin.getY());
        builder.allowRecompute();

        var pathfinder = builder.build(world);
        var posSet = new HashSet<BlockPos>();

        for (int i = 0; i < tries; i++) {
            pathfinder.compute();
            var output = pathfinder.getLastOutput();
            output.ifPresent(pathingOutput -> {
                posSet.addAll(pathingOutput.checkedBlocks());
            });
        }

        var maxProject = config.projection().get(random);

        posSet.forEach(blockPos -> tryPlace(world, config.cover().getBlockState(random, blockPos), blockPos, maxProject));

        return true;
    }

    public void tryPlace(StructureWorldAccess world, BlockState state, BlockPos pos, int maxProjection) {
        int adjustments = 0;
        while (adjustments <= maxProjection) {
            var floor = pos.down();

            if(!world.getBlockState(pos).getMaterial().isReplaceable()) {
                pos = pos.up();
            }
            else if(world.getBlockState(floor).isAir()) {
                pos = floor;
            }
            else if(state.canPlaceAt(world, pos) && world.getBlockState(floor).isFullCube(world, floor)) {
                world.setBlockState(pos, state, Block.NOTIFY_LISTENERS);
                break;
            }

            adjustments++;
        }
    }
}
