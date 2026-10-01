package net.id.paradise_lost.world.feature.tree.placers;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradise_lost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import java.util.List;
import java.util.function.BiConsumer;

public class OvergrownTrunkPlacer extends TrunkPlacer {

    @SuppressWarnings("CodeBlock2Expr")
    public static final MapCodec<OvergrownTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
        return instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((placer) -> {
            return placer.baseHeight;
        }), Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((placer) -> {
            return placer.heightRandA;
        }), Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((placer) -> {
            return placer.heightRandB;
        }), BlockStateProvider.CODEC.fieldOf("overgrowth").forGetter((placer) -> {
            return placer.overgrowthProvider;
        }), Codec.floatRange(0, 1).fieldOf("chance").forGetter((placer) -> {
            return placer.overgrowthChance;
        })).apply(instance, OvergrownTrunkPlacer::new);
    });

    private final BlockStateProvider overgrowthProvider;
    private final float overgrowthChance;

    public OvergrownTrunkPlacer(int i, int j, int k, BlockStateProvider overgrowthProvider, float overgrowthChance) {
        super(i, j, k);
        this.overgrowthProvider = overgrowthProvider;
        this.overgrowthChance = overgrowthChance;
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return ParadiseLostTreeHell.OVERGROWN_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader world, BiConsumer<BlockPos, BlockState> replacer, RandomSource random, int height, BlockPos startPos, TreeConfiguration config) {
        setDirtAt(world, replacer, random, startPos.below(), config);

        for (int i = 0; i < height; ++i) {
            var curPos = startPos.above(i);
            placeLog(world, replacer, random, curPos, config);

            if (i > 0 && i < (height * 0.7)) {
                var chance = overgrowthChance;

                for (Direction dir : Direction.values()) {
                    if (dir.get2DDataValue() >= 0 && random.nextFloat() <= chance) {
                        var tempPos = curPos.relative(dir);

                        if (TreeFeature.validTreePos(world, tempPos)) {
                            placeLog(world, replacer, random, tempPos, config, (state -> {
                                var overgrowth = overgrowthProvider.getState(random, tempPos);

                                if (overgrowth.hasProperty(BlockStateProperties.HORIZONTAL_FACING)) {
                                    overgrowth = overgrowth.setValue(BlockStateProperties.HORIZONTAL_FACING, dir.getOpposite());
                                }

                                return overgrowth;
                            }));
                            chance /= 4;
                        }
                    }
                }
            }
        }

        return ImmutableList.of(new FoliagePlacer.FoliageAttachment(startPos.above(height), 0, false));
    }
}
