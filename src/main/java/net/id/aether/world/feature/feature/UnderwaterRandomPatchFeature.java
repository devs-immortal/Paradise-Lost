package net.id.aether.world.feature.feature;

import com.mojang.serialization.Codec;
import net.id.aether.mixin.world.BlockFilterPlacementModifierAccessor;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.decorator.BlockFilterPlacementModifier;
import net.minecraft.world.gen.decorator.DecoratorContext;
import net.minecraft.world.gen.decorator.PlacementModifier;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.RandomPatchFeatureConfig;
import net.minecraft.world.gen.feature.SimpleBlockFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;
import java.util.Optional;
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
        // It's not pretty, but this is how it's done.
        List<PlacementModifier> modifiers= randomPatchFeatureConfig.feature().get().getPlacementModifiers();
        SimpleBlockFeatureConfig cfg = ((SimpleBlockFeatureConfig) randomPatchFeatureConfig.feature().get().getDecoratedFeatures().findFirst().get().getConfig());
        BlockState blockState = cfg.toPlace().getBlockState(random, origin);
        BlockPos projectedOrigin;

        projectedOrigin = origin;

        int i = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();

        for(int j = 0; j < randomPatchFeatureConfig.tries(); ++j) {

            mutable.set(projectedOrigin, random.nextInt(randomPatchFeatureConfig.xzSpread() + 1) - random.nextInt(randomPatchFeatureConfig.xzSpread() + 1), random.nextInt(randomPatchFeatureConfig.ySpread() + 1) - random.nextInt(randomPatchFeatureConfig.ySpread() + 1), random.nextInt(randomPatchFeatureConfig.xzSpread() + 1) - random.nextInt(randomPatchFeatureConfig.xzSpread() + 1));

            BlockPos floorPos = mutable.down();
            BlockState floor = structureWorldAccess.getBlockState(floorPos);

            BlockFilterPlacementModifier filter = null;
            for (var modifier : modifiers) {
                if (modifier instanceof BlockFilterPlacementModifier bfpm) {
                    filter = bfpm;
                    break;
                }
            }

            // throws an error if there is no block filter. So... give it a block filter.
            // this is also not very pretty. I'm sure there's a better way to do this, but this should also work.
            if (structureWorldAccess.isWater(mutable) && ((BlockFilterPlacementModifierAccessor) filter).shouldPlace(new DecoratorContext(structureWorldAccess, context.getGenerator(), Optional.of(randomPatchFeatureConfig.feature().get())), random, floorPos)) {
                randomPatchFeatureConfig.feature().get().generate(structureWorldAccess, context.getGenerator(), random, mutable);
                ++i;
            }

        }

        return i > 0;
    }
}
