package com.aether.world.feature.tree.placers;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherLeavesBlock;
import com.aether.world.feature.tree.AetherTreeHell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.Set;

import static com.aether.blocks.natural.AetherHangerBlock.TIP;

public class WisteriaFoliagePlacer extends FoliagePlacer {

    public static final Codec<WisteriaFoliagePlacer> CODEC = RecordCodecBuilder.create(instance ->
            fillFoliagePlacerFields(instance).apply(instance, WisteriaFoliagePlacer::new));

    public WisteriaFoliagePlacer(UniformIntDistribution radius, UniformIntDistribution offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return AetherTreeHell.WISTERIA_FOLIAGE;
    }

    @Override
    protected void generate(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int offset, BlockBox box) {
        if(radius <= 3)
            radius = 3;



        radius -= treeNode.getFoliageRadius();
        BlockPos nodePos = treeNode.getCenter();
        BlockPos altNodePos = nodePos.add(0, 1, 0);
        BlockState leafBlock = config.leavesProvider.getBlockState(random, nodePos);
        BlockState hanger = Blocks.AIR.getDefaultState();

        if(leafBlock.getBlock() instanceof AetherLeavesBlock) {
            hanger = AetherLeavesBlock.getHanger(leafBlock.getBlock());
        }

        for(int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = 0; k < radius; k++) {
                    BlockPos offPos = nodePos.add(Math.signum(i) * Math.abs(i)-k, k, Math.signum(j) * Math.abs(j)-k);
                    if((world.testBlockState(offPos, AbstractBlock.AbstractBlockState::isAir) || TreeFeature.canReplace(world, offPos)) && offPos.isWithinDistance(random.nextBoolean() ? nodePos : altNodePos, radius)) {
                        world.setBlockState(offPos, leafBlock, 19);
                        leaves.add(offPos);
                    }
                }
            }
        }
        int flip = random.nextInt();
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                BlockPos offPos = nodePos.add(i, 0, j);
                if(leaves.contains(offPos) && random.nextBoolean()) {
                    offPos.down(2);
                    int hangerLength = 1 + random.nextInt(2);
                    int step = 0;
                    while (step <= hangerLength && TreeFeature.canReplace(world, offPos)) {
                        world.setBlockState(offPos, hanger.with(TIP, false), 19);
                        offPos = offPos.down();
                        step++;
                    }
                    world.setBlockState(offPos.up(), hanger.with(TIP, true), 19);
                }
            }
        }
    }

    @Override
    public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
        return 0;
    }

    @Override
    protected boolean isInvalidForLeaves(Random random, int baseHeight, int dx, int y, int dz, boolean giantTrunk) {
        return false;
    }
}
