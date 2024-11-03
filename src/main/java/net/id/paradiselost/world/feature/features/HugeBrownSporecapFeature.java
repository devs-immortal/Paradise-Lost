package net.id.paradiselost.world.feature.features;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.ConnectingBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;

public class HugeBrownSporecapFeature extends HugeMushroomFeature {
    public HugeBrownSporecapFeature(Codec<HugeMushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected int getCapSize(int i, int j, int capSize, int y) {
        return capSize <= 3 ? 0 : capSize;
    }

    @Override
    protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
        BlockPos stemTop = start.up(y);
        BlockState capState = config.capProvider.get(random, stemTop).with(MushroomBlock.DOWN, false);
        // bottom level
        for (Direction d : ConnectingBlock.FACING_PROPERTIES.keySet()) {
            if (!d.getAxis().isHorizontal()) continue;
            BlockPos layer = stemTop.down().offset(d, 3);
            BlockPos layer2 = stemTop.down().offset(d, 2);
            this.setBlockState(world, layer, capState);
            this.setBlockState(world, layer.offset(d.rotateClockwise(Direction.Axis.Y)), capState);
            this.setBlockState(world, layer.offset(d.rotateCounterclockwise(Direction.Axis.Y)), capState);
            this.setBlockState(world, layer2.offset(d.rotateClockwise(Direction.Axis.Y), 2), capState);
            BlockState insideCapState = capState.with(ConnectingBlock.FACING_PROPERTIES.get(d.getOpposite()), false);
            this.setBlockState(world, layer2, insideCapState);
            this.setBlockState(world, layer2.offset(d.rotateClockwise(Direction.Axis.Y)), insideCapState);
            this.setBlockState(world, layer2.offset(d.rotateCounterclockwise(Direction.Axis.Y)), insideCapState);
        }
        // middle level
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                this.setBlockState(world, stemTop.north(i).east(j), capState);
            }
        }
        // top level
        this.setBlockState(world, stemTop.up(), capState);
    }
}
