package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.aether.Aether;
import net.id.aether.tag.AetherBlockTags;
import net.id.aether.world.feature.config.GroundcoverFeatureConfig;
import net.id.incubus_core.worldgen.BiFeatureConfig;
import net.minecraft.block.BlockState;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class GroundcoverFeature extends Feature<GroundcoverFeatureConfig> {

    public static final Codec<GroundcoverFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("states").forGetter(GroundcoverFeatureConfig::states),
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(GroundcoverFeatureConfig::size),
            IntProvider.VALUE_CODEC.fieldOf("spacing").forGetter(GroundcoverFeatureConfig::spacing)
    ).apply(instance, GroundcoverFeatureConfig::new));

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
            if(pos.getSquaredDistance(center) > Math.pow(radius, 2))
                return;

            boolean add = false;

            if(pos.getManhattanDistance(center) == radius && random.nextFloat() < 0.1F)
                add = true;

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

            BlockPos placement = new BlockPos(pos.getX(), context.getGenerator().getHeight(pos.getX(), pos.getZ(), Heightmap.Type.WORLD_SURFACE_WG, world) - 1, pos.getZ());

            if(world.testBlockState(placement, state -> AetherBlockTags.BASE_REPLACEABLES.contains(state.getBlock()))) {
                if(add)
                    centers.add(placement);

                var state = config.states().getBlockState(random, placement);

                world.setBlockState(placement, state, 2);
                world.getBlockTickScheduler().schedule(placement, state.getBlock(), 0);
            }
        });
        return centers;
    }
}
