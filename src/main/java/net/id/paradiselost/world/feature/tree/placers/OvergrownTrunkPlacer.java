package net.id.paradiselost.world.feature.tree.placers;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.world.feature.tree.ParadiseLostTreeHell;
import net.minecraft.block.BlockState;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.TestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;

import java.util.List;
import java.util.function.BiConsumer;

public class OvergrownTrunkPlacer extends TrunkPlacer {

    @SuppressWarnings("CodeBlock2Expr")
    public static final Codec<OvergrownTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codec.intRange(0, 32).fieldOf("base_height").forGetter((placer) -> {
            return placer.baseHeight;
        }), Codec.intRange(0, 24).fieldOf("height_rand_a").forGetter((placer) -> {
            return placer.firstRandomHeight;
        }), Codec.intRange(0, 24).fieldOf("height_rand_b").forGetter((placer) -> {
            return placer.secondRandomHeight;
        }), BlockStateProvider.TYPE_CODEC.fieldOf("overgrowth").forGetter((placer) -> {
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
    protected TrunkPlacerType<?> getType() {
        return ParadiseLostTreeHell.OVERGROWN_TRUNK;
    }

    @Override
    public List<FoliagePlacer.TreeNode> generate(TestableWorld world, BiConsumer<BlockPos, BlockState> replacer, Random random, int height, BlockPos startPos, TreeFeatureConfig config) {
        setToDirt(world, replacer, random, startPos.down(), config);

        for (int i = 0; i < height; ++i) {
            var curPos = startPos.up(i);
            getAndSetState(world, replacer, random, curPos, config);

            if (i > 0 && i < (height * 0.7)) {
                var chance = overgrowthChance;

                for (Direction dir : Direction.values()) {
                    if (dir.getHorizontal() >= 0 && random.nextFloat() <= chance) {
                        var tempPos = curPos.offset(dir);

                        if (TreeFeature.canReplace(world, tempPos)) {
                            getAndSetState(world, replacer, random, tempPos, config, (state -> {
                                var overgrowth = overgrowthProvider.get(random, tempPos);

                                if (overgrowth.contains(Properties.HORIZONTAL_FACING)) {
                                    overgrowth = overgrowth.with(Properties.HORIZONTAL_FACING, dir.getOpposite());
                                }

                                return overgrowth;
                            }));
                            chance /= 4;
                        }
                    }
                }
            }
        }

        return ImmutableList.of(new FoliagePlacer.TreeNode(startPos.up(height), 0, false));
    }
}
