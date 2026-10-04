package net.id.paradise_lost.world.feature.tree.placers;

import com.google.common.collect.Sets;
import com.mojang.datafixers.Products;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.block.natural.tree.ParadiseLostHangerBlock;
import net.id.paradise_lost.block.natural.tree.ParadiseLostLeavesBlock;
import net.id.paradise_lost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import java.util.Set;

public class WisteriaFoliagePlacer extends FoliagePlacer {

    public static final MapCodec<WisteriaFoliagePlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> createCodec(instance).apply(instance, WisteriaFoliagePlacer::new));

    protected static <P extends WisteriaFoliagePlacer> Products.P2<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider> createCodec(RecordCodecBuilder.Instance<P> builder) {
        return foliagePlacerParts(builder);
    }

    public WisteriaFoliagePlacer(IntProvider radius, IntProvider offset) {
        super(radius, offset);
    }

    @Override
    protected FoliagePlacerType<?> type() {
        return ParadiseLostTreeHell.WISTERIA_FOLIAGE;
    }

    @Override
    protected void createFoliage(LevelSimulatedReader world, FoliageSetter placer, RandomSource random, TreeConfiguration config, int trunkHeight, FoliageAttachment treeNode, int foliageHeight, int radius, int offset) {
        Set<BlockPos> leaves = Sets.newHashSet();
        if (radius < 3) {
            radius = 3;
        }

        radius -= treeNode.radiusOffset();
        if (radius > 7) {
            radius = 7;
        }

        BlockPos nodePos = treeNode.pos();
        BlockPos altNodePos = nodePos.above(offset);

        BlockState leafBlock = config.foliageProvider.getState(random, nodePos);
        BlockState hanger = Blocks.AIR.defaultBlockState();

        if (leafBlock.getBlock() instanceof ParadiseLostLeavesBlock) {
            hanger = ParadiseLostLeavesBlock.getHanger(leafBlock);
        }

        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = 0; k < radius; k++) {
                    BlockPos offPos = nodePos.offset(i - k, k, j - k);
                    if ((world.isStateAtPosition(offPos, BlockBehaviour.BlockStateBase::isAir) || TreeFeature.validTreePos(world, offPos)) && offPos.closerThan(random.nextBoolean() ? nodePos : altNodePos, radius)) {
                        placer.set(offPos, leafBlock);
                        leaves.add(offPos);
                    }
                }
            }
        }
        for (int i = -radius; i < radius; i++) {
            for (int j = -radius; j < radius; j++) {
                BlockPos offPos = nodePos.offset(i, 0, j);
                if (leaves.contains(offPos) && random.nextBoolean()) {
                    offPos = offPos.below();
                    int hangerLength = random.nextInt((int) Math.max(2, trunkHeight / 3.0 * 2));
                    int step = 0;
                    while (step <= hangerLength && world.isStateAtPosition(offPos, BlockBehaviour.BlockStateBase::isAir)) {
                        placer.set(offPos, hanger.setValue(ParadiseLostHangerBlock.TIP, false));
                        offPos = offPos.below();
                        step++;
                    }
                    placer.set(offPos.above(), hanger.setValue(ParadiseLostHangerBlock.TIP, true));
                }
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int trunkHeight, TreeConfiguration config) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int baseHeight, int dx, int y, int dz, boolean giantTrunk) {
        return false;
    }
}
