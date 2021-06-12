package com.aether.world.feature;

import com.aether.world.feature.config.AercloudConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;

public class AercloudFeature extends Feature<AercloudConfig> {

    private static final Codec<AercloudConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(AercloudConfig::getCloudState),
            Codec.BOOL.fieldOf("flat").forGetter(AercloudConfig::isFlat),
            Codec.INT.fieldOf("maxRadius").forGetter(AercloudConfig::maxSegments),
            Codec.INT.fieldOf("y").forGetter(AercloudConfig::getY)
    ).apply(instance, AercloudConfig::new));

    public AercloudFeature() {
        super(CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<AercloudConfig> context) {
        return (createCloudBlob(context.level(), context.config().state, context.random(), context.origin(), 3, 10) || createCloudBlob(context.level(), context.config().state, context.random(), context.origin().north(3).east(), 3, 8));
    }

    private int randomSign(Random random) {
        int ran = random.nextInt(2);
        if (ran == 0) {
            return -1;
        } else {
            return 1;
        }
    }

    private boolean createCloudBlob(WorldGenLevel world, BlockState state, Random random, BlockPos center, int sizex, int sizez) {
        for (BlockPos blockPos : BlockPos.betweenClosed(center.offset(-sizex * 0.3, -sizex * 0.3, -sizez * 0.3), center.offset(sizex * 0.3, sizex * 0.3, sizez * 0.3))) {
            if (testElipsoid(sizex, sizex, sizez, blockPos, center) && (!(world.isEmptyBlock(blockPos) || world.getBlockState(blockPos) == state.getBlock().defaultBlockState()))) {
                return false;
            }
        }
        if (sizex > 0 && sizez > 1) {
            for (int z = -sizez; z <= sizez; z++) {
                int stuffsize = Math.round(sizex*(1f/(Math.max(Math.abs(((float)z)/7), 1))));
                for (int x = center.getX() - stuffsize; x <= center.getX() + stuffsize; x++) {
                    for (int y = center.getY() - Math.round(stuffsize*0.7f); y <= center.getY() + Math.round(stuffsize*0.7f); y++) {
                        if (!((x == center.getX() - stuffsize || x == center.getX() + stuffsize) && (y == center.getY() - stuffsize + 1 || y == center.getY() + stuffsize - 1))) {
                            if (world.getBlockState(new BlockPos(x, y, center.getZ() + z)).is(Blocks.AIR)) {
                                world.setBlock(new BlockPos(x, y, center.getZ() + z), state, 2);
                            }
                        }
                    }
                }
            }
            center = center.below((random.nextInt(1)+1)*randomSign(random));
            center = center.east((random.nextInt(3)+3)*randomSign(random));
            center = center.north((random.nextInt(3)+3)*randomSign(random));
            createCloudBlob(world, state, random, center, sizex-random.nextInt(1), sizez-random.nextInt(1)-1);
        }

        return true;
    }

//    @Override
//    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos core, AercloudConfig config) {
//        int radius = (random.nextInt((int) (config.maxRadius / 1.5)) + config.maxRadius / 4) + 1;
//        int baseHeight = (int) (radius / (random.nextDouble() + 1.25));
//
//        for (BlockPos blockPos : BlockPos.iterate(core.add(-radius * 1.1, -baseHeight * 1.1, -radius * 1.1), core.add(radius * 1.1, baseHeight * 1.1, radius * 1.1))) {
//            if (testElipsoid(radius, baseHeight, radius, blockPos, core) && !world.isAir(blockPos)) {
//                return false;
//            }
//        }
//
//        final Queue<CloudInfo> nodes = new LinkedList<>();
//
//        nodes.add(new CloudInfo(core, radius, baseHeight));
//
//        for (int i = 0; i < 6 && (random.nextBoolean() || i == 0); i++) {
//            int offset = radius - random.nextInt((int) Math.ceil(radius / 2.0));
//            nodes.add(new CloudInfo(core.add(offset * Math.cos(random.nextDouble() * 2 * Math.PI), random.nextInt(5) - 2, offset * Math.sin(random.nextDouble() * 2 * Math.PI)), (int) (radius * (random.nextDouble() / 4 + 0.75)), (int) (baseHeight * (random.nextDouble() / 2 + 0.5))));
//        }
//
//        final Queue<CloudInfo> secNodes = new LinkedList<>();
//
//        for (CloudInfo node : nodes) {
//            int offset = node.radius - random.nextInt((int) Math.ceil(node.radius / 2.0));
//            secNodes.add(new CloudInfo(node.center.add(offset * Math.cos(random.nextDouble() * 2 * Math.PI), random.nextInt(5) - 2, offset * Math.sin(random.nextDouble() * 2 * Math.PI)), (int) (node.radius * (random.nextDouble() / 4 + 0.75)), (int) (node.yMod * (random.nextDouble() + 0.5))));
//        }
//
//        nodes.addAll(secNodes);
//
//        while (!nodes.isEmpty()) {
//            CloudInfo node = nodes.poll();
//
//            BlockPos heart = node.center;
//            int nodeRadius = node.radius;
//            int nodeHeight = node.yMod;
//
//            for (BlockPos blockPos : BlockPos.iterate(heart.add(-nodeRadius, -nodeHeight, -nodeRadius), heart.add(nodeRadius, nodeHeight, nodeRadius))) {
//                if (testElipsoid(radius, baseHeight, radius + random.nextInt(5) - 2, blockPos, heart) && world.isAir(blockPos)) {
//                    world.setBlockState(blockPos, config.state, 2);
//                }
//            }
//        }
//
//        return true;
//    }

    //private void createCloudAdditively(WorldAccess world, BlockState cloud, Random random, int radius, int rarityMod, BlockPos leaf, BlockPos core,) {
    //    if(testSphere(radius, leaf, core)) {
    //        if(repeat && random.nextInt(rarityMod) == 0)
    //            nodes.add(leaf);
    //
    //    }
    //}

    private static class CloudInfo {

        public final int radius;
        public final BlockPos center;
        public final int yMod;

        public CloudInfo(BlockPos center, int radius, int yMod) {
            this.radius = radius;
            this.center = center;
            this.yMod = yMod;
        }
    }

    private boolean testElipsoid(int a, int b, int c, BlockPos test, BlockPos center) {
        int x = Math.abs(test.getX() - center.getX());
        int y = Math.abs(test.getY() - center.getY());
        int z = Math.abs(test.getZ() - center.getZ());
        return Math.pow(x, 2) / Math.pow(a, 2) + Math.pow(y, 2) / Math.pow(b, 2) + Math.pow(z, 2) / Math.pow(c, 2)  <= 1;
    }
}