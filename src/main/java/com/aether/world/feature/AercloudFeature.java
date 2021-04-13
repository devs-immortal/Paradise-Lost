package com.aether.world.feature;

import com.aether.world.feature.config.AercloudConfig;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.LinkedList;
import java.util.Queue;
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
    public boolean generate(StructureWorldAccess world, ChunkGenerator generator, Random random, BlockPos core, AercloudConfig config) {
        int radius = (random.nextInt((int) (config.maxRadius / 1.5)) + config.maxRadius / 4) + 1;
        int baseHeight = (int) (radius / (random.nextDouble() + 1.25));

        for (BlockPos blockPos : BlockPos.iterate(core.add(-radius * 1.1, -baseHeight * 1.1, -radius * 1.1), core.add(radius * 1.1, baseHeight * 1.1, radius * 1.1))) {
            if (testElipsoid(radius, baseHeight, radius, blockPos, core) && !world.isAir(blockPos)) {
                return false;
            }
        }

        final Queue<CloudInfo> nodes = new LinkedList<>();

        nodes.add(new CloudInfo(core, radius, baseHeight));

        for (int i = 0; i < 6 && (random.nextBoolean() || i == 0); i++) {
            int offset = radius - random.nextInt((int) Math.ceil(radius / 2.0));
            nodes.add(new CloudInfo(core.add(offset * Math.cos(random.nextDouble() * 2 * Math.PI), random.nextInt(5) - 2, offset * Math.sin(random.nextDouble() * 2 * Math.PI)), (int) (radius * (random.nextDouble() / 4 + 0.75)), (int) (baseHeight * (random.nextDouble() / 2 + 0.5))));
        }

        final Queue<CloudInfo> secNodes = new LinkedList<>();

        for (CloudInfo node : nodes) {
            int offset = node.radius - random.nextInt((int) Math.ceil(node.radius / 2.0));
            secNodes.add(new CloudInfo(node.center.add(offset * Math.cos(random.nextDouble() * 2 * Math.PI), random.nextInt(5) - 2, offset * Math.sin(random.nextDouble() * 2 * Math.PI)), (int) (node.radius * (random.nextDouble() / 4 + 0.75)), (int) (node.yMod * (random.nextDouble() + 0.5))));
        }

        nodes.addAll(secNodes);

        while (!nodes.isEmpty()) {
            CloudInfo node = nodes.poll();

            BlockPos heart = node.center;
            int nodeRadius = node.radius;
            int nodeHeight = node.yMod;

            for (BlockPos blockPos : BlockPos.iterate(heart.add(-nodeRadius, -nodeHeight, -nodeRadius), heart.add(nodeRadius, nodeHeight, nodeRadius))) {
                if (testElipsoid(radius, baseHeight, radius + random.nextInt(5) - 2, blockPos, heart) && world.isAir(blockPos)) {
                    world.setBlockState(blockPos, config.state, 2);
                }
            }
        }

        return true;
    }

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