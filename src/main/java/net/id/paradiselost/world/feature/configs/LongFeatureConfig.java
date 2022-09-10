package net.id.paradiselost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

import java.util.List;

public record LongFeatureConfig(IntProvider size, BlockStateProvider body, BlockStateProvider top, BlockStateProvider shell, float topChance, float shellChance, List<BlockState> validFloor) implements FeatureConfig {
    public static final Codec<LongFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(LongFeatureConfig::size),
            BlockStateProvider.TYPE_CODEC.fieldOf("body").forGetter(LongFeatureConfig::body),
            BlockStateProvider.TYPE_CODEC.fieldOf("top").forGetter(LongFeatureConfig::top),
            BlockStateProvider.TYPE_CODEC.fieldOf("shell").forGetter(LongFeatureConfig::shell),
            Codec.FLOAT.fieldOf("topChance").forGetter(LongFeatureConfig::topChance),
            Codec.FLOAT.fieldOf("shellChance").forGetter(LongFeatureConfig::shellChance),
            Codec.list(BlockState.CODEC).fieldOf("floor").forGetter(LongFeatureConfig::validFloor)
    ).apply(instance, LongFeatureConfig::new));
}
