package com.aether.world.feature.tree.placers;

import com.aether.blocks.AetherBlocks;
import com.aether.blocks.natural.AetherLeavesBlock;
import com.aether.blocks.natural.AuralLeavesBlock;
import com.aether.world.feature.tree.AetherTreeHell;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;

import static com.aether.blocks.natural.AetherHangerBlock.TIP;

public class WisteriaFoliagePlacer extends FoliagePlacer {

    public static final Codec<WisteriaFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
            return foliagePlacerParts(instance).apply(instance, WisteriaFoliagePlacer::new); });

    /*
    public static final Codec<DarkOakFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) -> {
      return fillFoliagePlacerFields(instance).apply(instance, (BiFunction)(DarkOakFoliagePlacer::new));
   });
     */

    public WisteriaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return AetherTreeHell.WISTERIA_FOLIAGE;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, Random random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
        // TODO: FIXME - 1.17 (Using Stub code for now)
        boolean bl = treeNode.doubleTrunk();
        BlockPos blockPos = treeNode.pos().above(offset);
        this.placeLeavesRow(world, replacer, random, config, blockPos, radius + treeNode.radiusOffset(), -1 - foliageHeight, bl);
        this.placeLeavesRow(world, replacer, random, config, blockPos, radius - 1, -foliageHeight, bl);
        this.placeLeavesRow(world, replacer, random, config, blockPos, radius + treeNode.radiusOffset() - 1, 0, bl);
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
//            hanger = AetherLeavesBlock.getHanger(leafBlock);
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
    public int foliageHeight(Random random, int trunkHeight, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(Random random, int baseHeight, int dx, int y, int dz, boolean giantTrunk) {
        return false;
    }
}
