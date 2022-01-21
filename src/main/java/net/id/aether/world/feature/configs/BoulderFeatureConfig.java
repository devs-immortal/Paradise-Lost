package net.id.aether.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record BoulderFeatureConfig(BlockStateProvider body, IntProvider tries, IntProvider size) implements FeatureConfig {
    public static final Codec<BoulderFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("body").forGetter(BoulderFeatureConfig::body),
            IntProvider.VALUE_CODEC.fieldOf("tries").forGetter(BoulderFeatureConfig::tries),
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(BoulderFeatureConfig::size)
    ).apply(instance, BoulderFeatureConfig::new));
}
