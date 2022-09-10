package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.id.paradiselost.tag.ParadiseLostBlockTags;
import net.id.paradiselost.world.feature.configs.GroundcoverFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.tick.OrderedTick;

import java.util.LinkedList;
import java.util.Queue;

public class GroundcoverFeature extends Feature<GroundcoverFeatureConfig> {

    public GroundcoverFeature(Codec<GroundcoverFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<GroundcoverFeatureConfig> context) {
        StructureWorldAccess world = context.getWorld();

        var center = context.getOrigin().down();
        var config = context.getConfig();
        Random random = context.getRandom();

        int radius = config.size().get(random);

        Queue<BlockPos> centers = generateCircleAndCenters(world, center, radius, random, context);

        radius = config.size().get(random);

        Queue<BlockPos> holder = new LinkedList<>();

        for (BlockPos blockPos : centers) {
            holder.addAll(generateCircleAndCenters(world, blockPos, radius, random, context));
        }

        centers.clear();
        centers.addAll(holder);

        radius = config.size().get(random);

        for (BlockPos newCenter : centers) {
            generateCircleAndCenters(world, newCenter, radius, random, context);
        }

        return true;
    }

    private Queue<BlockPos> generateCircleAndCenters(StructureWorldAccess world, BlockPos center, int radius, Random random, FeatureContext<GroundcoverFeatureConfig> context) {

        var config = context.getConfig();

        Queue<BlockPos> centers = new LinkedList<>();
        BlockPos.iterateOutwards(center, radius, 0, radius).forEach(pos -> {
            if(pos.getSquaredDistance(center) > Math.pow(radius, 2)) {
                return;
            }

            boolean add = pos.getManhattanDistance(center) == radius && random.nextFloat() < 0.1F;
    
            //if(world.isAir(pos) || world.isWater(pos) || !world.isWater(pos.up()) || !world.isAir(pos.up())) {
            //    boolean fail = true;
            //    int tries = 5;
//
            //    while(tries >= -4) {
            //        tries--;
            //        pos = pos.offset(Direction.Axis.Y, tries);
            //        if(!world.isAir(pos) && !world.isWater(pos) && (world.isWater(pos.up()) || world.isAir(pos.up()))) {
            //            fail = false;
            //            break;
            //        }
            //    }
//
            //    if(fail)
            //        return;
            //}

            BlockPos placement = pos.withY(0 /*context.getGenerator().getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world) - 1*/);

            if(world.testBlockState(placement, state -> state.isIn(ParadiseLostBlockTags.BASE_REPLACEABLES))) {
                if(add) {
                    centers.add(placement);
                }

                var state = config.states().getBlockState(random, placement);

                world.setBlockState(placement, state, Block.NOTIFY_LISTENERS);
                world.getBlockTickScheduler().scheduleTick(OrderedTick.create(state.getBlock(), placement));
            }
        });
        return centers;
    }
}
