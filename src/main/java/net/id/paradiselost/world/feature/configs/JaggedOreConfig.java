package net.id.paradiselost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record JaggedOreConfig(BlockStateProvider block, IntProvider height, IntProvider width, IntProvider length, IntProvider lengthOffset) implements FeatureConfig {
    public static final Codec<JaggedOreConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("block").forGetter(JaggedOreConfig::block),
            IntProvider.VALUE_CODEC.fieldOf("height").forGetter(JaggedOreConfig::height),
            IntProvider.VALUE_CODEC.fieldOf("width").forGetter(JaggedOreConfig::width),
            IntProvider.VALUE_CODEC.fieldOf("length").forGetter(JaggedOreConfig::length),
            IntProvider.VALUE_CODEC.fieldOf("lengthOffset").forGetter(JaggedOreConfig::lengthOffset)
    ).apply(instance, JaggedOreConfig::new));
}
