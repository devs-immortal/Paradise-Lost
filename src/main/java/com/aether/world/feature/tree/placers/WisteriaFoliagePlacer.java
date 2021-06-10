package com.aether.world.feature.tree.placers;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherLeavesBlock;
import com.aether.blocks.natural.AuralLeavesBlock;
import com.aether.world.feature.tree.AetherTreeHell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.BlockView;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.aether.blocks.natural.AetherHangerBlock.TIP;

public class WisteriaFoliagePlacer extends FoliagePlacer {

    public static final Codec<WisteriaFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
            return fillFoliagePlacerFields(instance).apply(instance, WisteriaFoliagePlacer::new); });

    /*
    public static final Codec<DarkOakFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
      return fillFoliagePlacerFields(instance).apply(instance, (BiFunction)(DarkOakFoliagePlacer::new));
   });
     */

    public WisteriaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return AetherTreeHell.WISTERIA_FOLIAGE;
    }

    @Override
    protected void generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        // TODO: FIXME - 1.17 (Using Stub code for now)
        boolean bl = treeNode.isGiantTrunk();
        BlockPos blockPos = treeNode.getCenter().up(offset);
        this.generateSquare(world, replacer, random, config, blockPos, radius + treeNode.getFoliageRadius(), -1 - foliageHeight, bl);
        this.generateSquare(world, replacer, random, config, blockPos, radius - 1, -foliageHeight, bl);
        this.generateSquare(world, replacer, random, config, blockPos, radius + treeNode.getFoliageRadius() - 1, 0, bl);
//        if(radius <= 3)
//            radius = 3;
//
//        radius -= treeNode.getFoliageRadius();
//        BlockPos nodePos = treeNode.getCenter();
//        BlockPos altNodePos = nodePos.add(0, 1, 0);
//        BlockState leafBlock = config.leavesProvider.getBlockState(random, nodePos);
//        BlockState hanger = Blocks.AIR.getDefaultState();
//
//        if(leafBlock.getBlock() instanceof AetherLeavesBlock || leafBlock.getBlock() instanceof AuralLeavesBlock) {
//            hanger = AetherLeavesBlock.getHanger(leafBlock.getBlock());
//        }
//
//        for(int i = -radius; i <= radius; i++) {
//            for (int j = -radius; j <= radius; j++) {
//                for (int k = 0; k < radius; k++) {
//                    BlockPos offPos = nodePos.add(Math.signum(i) * Math.abs(i)-k, k, Math.signum(j) * Math.abs(j)-k);
//                    if((world.testBlockState(offPos, AbstractBlock.AbstractBlockState::isAir) || TreeFeature.canReplace(world, offPos)) && offPos.isWithinDistance(random.nextBoolean() ? nodePos : altNodePos, radius)) {
//                        world.setBlockState(offPos, leafBlock, 19);
//                        leaves.add(offPos);
//                    }
//                }
//            }
//        }
//        for (int i = -radius; i < radius; i++) {
//            for (int j = -radius; j < radius; j++) {
//                BlockPos offPos = nodePos.add(i, 0, j);
//                if(leaves.contains(offPos) && random.nextBoolean()) {
//                    offPos = offPos.down();
//                    int hangerLength = 1 + random.nextInt(2);
//                    int step = 0;
//                    while (step <= hangerLength && world.testBlockState(offPos, AbstractBlock.AbstractBlockState::isAir)) {
//                        world.setBlockState(offPos, hanger.with(TIP, false), 19);
//                        offPos = offPos.down();
//                        step++;
//                    }
//                    world.setBlockState(offPos.up(), hanger.with(TIP, true), 19);
//                }
//            }
//        }
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
