package net.id.paradiselost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record GroundcoverFeatureConfig(BlockStateProvider states, IntProvider size, IntProvider spacing) implements FeatureConfig {
    public static final Codec<GroundcoverFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.TYPE_CODEC.fieldOf("states").forGetter(GroundcoverFeatureConfig::states),
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(GroundcoverFeatureConfig::size),
            IntProvider.VALUE_CODEC.fieldOf("spacing").forGetter(GroundcoverFeatureConfig::spacing)
    ).apply(instance, GroundcoverFeatureConfig::new));
}
