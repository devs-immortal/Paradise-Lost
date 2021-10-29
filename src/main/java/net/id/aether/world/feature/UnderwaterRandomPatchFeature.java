package net.id.aether.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.Random;

public class UnderwaterRandomPatchFeature extends Feature<RandomPatchFeatureConfig> {

    public UnderwaterRandomPatchFeature(Codec<RandomPatchFeatureConfig> configCodec) {
        super(configCodec);
    }

    public boolean generate(FeatureContext<RandomPatchFeatureConfig> context) {
        RandomPatchFeatureConfig randomPatchFeatureConfig = context.getConfig();
        Random random = context.getRandom();
        BlockPos origin = context.getOrigin();
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockState blockState = randomPatchFeatureConfig.stateProvider.getBlockState(random, origin);
        BlockPos projectedOrigin;

        if (randomPatchFeatureConfig.project) {
            projectedOrigin = structureWorldAccess.getTopPosition(Heightmap.Type.WORLD_SURFACE_WG, origin);
        } else {
            projectedOrigin = origin;
        }

        int i = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int j = 0; j < randomPatchFeatureConfig.tries; ++j) {

            mutable.set(projectedOrigin, random.nextInt(randomPatchFeatureConfig.spreadX + 1) - random.nextInt(randomPatchFeatureConfig.spreadX + 1), random.nextInt(randomPatchFeatureConfig.spreadY + 1) - random.nextInt(randomPatchFeatureConfig.spreadY + 1), random.nextInt(randomPatchFeatureConfig.spreadZ + 1) - random.nextInt(randomPatchFeatureConfig.spreadZ + 1));

            BlockPos floorPos = mutable.down();
            BlockState floor = structureWorldAccess.getBlockState(floorPos);
            if (structureWorldAccess.isWater(mutable) && randomPatchFeatureConfig.whitelist.contains(floor.getBlock())) {
                randomPatchFeatureConfig.blockPlacer.generate(structureWorldAccess, mutable, blockState, random);
                ++i;
            }

        }

        return i > 0;
    }
}
