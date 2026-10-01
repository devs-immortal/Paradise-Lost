package net.id.paradise_lost.world.feature.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeBrownSporecapFeature extends AbstractHugeMushroomFeature {
    public HugeBrownSporecapFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int j, int capSize, int y) {
        return capSize <= 3 ? 0 : capSize;
    }

    @Override
    protected void makeCap(LevelAccessor world, RandomSource random, BlockPos start, int y, BlockPos.MutableBlockPos mutable, HugeMushroomFeatureConfiguration config) {
        BlockPos stemTop = start.above(y);
        BlockState capState = config.capProvider.getState(random, stemTop).setValue(HugeMushroomBlock.DOWN, false);

        for (Direction d : PipeBlock.PROPERTY_BY_DIRECTION.keySet()) {
            if (!d.getAxis().isHorizontal()) continue;
            BlockPos layer = stemTop.below().relative(d, 3);
            BlockPos layer2 = stemTop.below().relative(d, 2);
            this.setBlock(world, layer, capState);
            this.setBlock(world, layer.relative(d.getClockWise(Direction.Axis.Y)), capState);
            this.setBlock(world, layer.relative(d.getCounterClockWise(Direction.Axis.Y)), capState);
            this.setBlock(world, layer2.relative(d.getClockWise(Direction.Axis.Y), 2), capState);
            BlockState insideCapState = capState.setValue(PipeBlock.PROPERTY_BY_DIRECTION.get(d.getOpposite()), false);
            this.setBlock(world, layer2, insideCapState);
            this.setBlock(world, layer2.relative(d.getClockWise(Direction.Axis.Y)), insideCapState);
            this.setBlock(world, layer2.relative(d.getCounterClockWise(Direction.Axis.Y)), insideCapState);
        }

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                this.setBlock(world, stemTop.north(i).east(j), capState);
            }
        }

        this.setBlock(world, stemTop.above(), capState);
    }
}
