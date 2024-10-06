package net.id.paradiselost.world.feature.tree.placers;

import com.google.common.collect.Sets;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostHangerBlock;
import net.id.paradiselost.blocks.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.BlobFoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;

import java.util.Set;

public class WisteriaFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<WisteriaFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> createCodec(instance).apply(instance, WisteriaFoliagePlacer::new));

    protected static <P extends WisteriaFoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> createCodec(RecordCodecBuilder.Instance<P> builder) {
        return fillFoliagePlacerFields(builder);
    }

    public WisteriaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> getType() {
        return ParadiseLostTreeHell.WISTERIA_FOLIAGE;
    }

    @Override
    protected void generate(TestableWorld world, BlockPlacer placer, Random random, TreeFeatureConfig config, int trunkHeight, TreeNode treeNode, int foliageHeight, int radius, int offset) {
        Set<BlockPos> leaves = Sets.newHashSet();
        if (radius < 3) {
            radius = 3;
        }

        radius -= treeNode.getFoliageRadius();
        if (radius > 7) {
            radius = 7;
        }

        BlockPos nodePos = treeNode.getCenter();
        BlockPos altNodePos = nodePos.up(offset);

        BlockState leafBlock = config.foliageProvider.get(random, nodePos);
        BlockState hanger = Blocks.AIR.getDefaultState();

        if (leafBlock.getBlock() instanceof ParadiseLostLeavesBlock) {
            hanger = ParadiseLostLeavesBlock.getHanger(leafBlock);
        }

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = 0; k < radius; k++) {
                    BlockPos offPos = nodePos.add(i - k, k, j - k);
                    if ((world.testBlockState(offPos, AbstractBlock.AbstractBlockState::isAir) || TreeFeature.canReplace(world, offPos)) && offPos.isWithinDistance(random.nextBoolean() ? nodePos : altNodePos, radius)) {
                        placer.placeBlock(offPos, leafBlock);
                        leaves.add(offPos);
                    }
                }
            }
        }
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                BlockPos offPos = nodePos.add(i, 0, j);
                if (leaves.contains(offPos) && random.nextBoolean()) {
                    offPos = offPos.down();
                    int hangerLength = random.nextInt((int) Math.max(2, trunkHeight / 3.0 * 2));
                    int step = 0;
                    while (step <= hangerLength && world.testBlockState(offPos, AbstractBlock.AbstractBlockState::isAir)) {
                        placer.placeBlock(offPos, hanger.with(ParadiseLostHangerBlock.TIP, false));
                        offPos = offPos.down();
                        step++;
                    }
                    placer.placeBlock(offPos.up(), hanger.with(ParadiseLostHangerBlock.TIP, true));
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
