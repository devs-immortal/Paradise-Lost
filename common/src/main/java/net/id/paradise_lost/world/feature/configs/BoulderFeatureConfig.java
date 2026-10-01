package net.id.paradise_lost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record BoulderFeatureConfig(BlockStateProvider body, IntProvider tries, IntProvider size) implements FeatureConfiguration {
    public static final Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("body").forGetter(BoulderFeatureConfig::body),
            IntProvider.CODEC.fieldOf("tries").forGetter(BoulderFeatureConfig::tries),
            IntProvider.CODEC.fieldOf("size").forGetter(BoulderFeatureConfig::size)
    ).apply(instance, BoulderFeatureConfig::new));
}
