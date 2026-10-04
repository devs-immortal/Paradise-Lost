package net.id.paradise_lost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record GroundcoverFeatureConfig(BlockStateProvider states, IntProvider size, IntProvider spacing) implements FeatureConfiguration {
    public static final Codec<GroundcoverFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockStateProvider.CODEC.fieldOf("states").forGetter(GroundcoverFeatureConfig::states),
            IntProvider.CODEC.fieldOf("size").forGetter(GroundcoverFeatureConfig::size),
            IntProvider.CODEC.fieldOf("spacing").forGetter(GroundcoverFeatureConfig::spacing)
    ).apply(instance, GroundcoverFeatureConfig::new));
}
