package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import net.id.aether.tag.AetherBlockTags;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.DeltaFeature;
import net.minecraft.world.gen.feature.DeltaFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Iterator;
import java.util.Random;

public class AetherDeltaFeature extends DeltaFeature {

    public AetherDeltaFeature(Codec<DeltaFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<DeltaFeatureConfig> context) {
        boolean bl = false;
        Random random = context.getRandom();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        DeltaFeatureConfig deltaFeatureConfig = context.getConfig();
        BlockPos blockPos = context.getOrigin();
        boolean bl2 = random.nextDouble() < 0.9D;
        int i = bl2 ? deltaFeatureConfig.getRimSize().get(random) : 0;
        int j = bl2 ? deltaFeatureConfig.getRimSize().get(random) : 0;
        boolean bl3 = bl2 && i != 0 && j != 0;
        int k = deltaFeatureConfig.getSize().get(random);
        int l = deltaFeatureConfig.getSize().get(random);
        int m = Math.max(k, l);

        for (BlockPos blockPos2 : BlockPos.iterateOutwards(blockPos, k, 0, l)) {
            if (blockPos2.getManhattanDistance(blockPos) > m) {
                break;
            }

            if (canPlace(structureWorldAccess, blockPos2, deltaFeatureConfig)) {
                if (bl3) {
                    bl = true;
                    this.setBlockState(structureWorldAccess, blockPos2, deltaFeatureConfig.getRim());
                }

                BlockPos blockPos3 = blockPos2.add(i, 0, j);
                if (canPlace(structureWorldAccess, blockPos3, deltaFeatureConfig)) {
                    bl = true;
                    this.setBlockState(structureWorldAccess, blockPos3, deltaFeatureConfig.getContents());
                }
            }
        }

        return bl;
    }

    private static boolean canPlace(WorldAccess world, BlockPos pos, DeltaFeatureConfig config) {
        BlockState blockState = world.getBlockState(pos);
        if (blockState.isOf(config.getContents().getBlock())) {
            return false;
        } else if (blockState.getHardness(world, pos) <= -1 || AetherBlockTags.FLUID_IRREPLACEABLES.contains(blockState.getBlock())) {
            return false;
        } else {
            Direction[] var4 = Direction.values();
            int var5 = var4.length;

            for (Direction direction : var4) {
                boolean bl = world.getBlockState(pos.offset(direction)).isAir();
                if (bl && direction != Direction.UP || !bl && direction == Direction.UP) {
                    return false;
                }
            }

            return true;
        }
    }
}
