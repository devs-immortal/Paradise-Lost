package net.id.paradise_lost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record FrozenAwareFeatureConfig(boolean frozen) implements FeatureConfiguration {
    public static final Codec<FrozenAwareFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("frozen").forGetter(FrozenAwareFeatureConfig::frozen)
    ).apply(instance, FrozenAwareFeatureConfig::new));

    public static final FrozenAwareFeatureConfig THAWED = new FrozenAwareFeatureConfig(false);
    public static final FrozenAwareFeatureConfig FROZEN = new FrozenAwareFeatureConfig(true);
}
