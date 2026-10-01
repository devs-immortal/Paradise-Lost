package net.id.paradise_lost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record LongFeatureConfig(IntProvider size, BlockStateProvider body, BlockStateProvider top, BlockStateProvider shell, float topChance, float shellChance, HolderSet<Block> validFloor) implements FeatureConfiguration {
    public static final Codec<LongFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.CODEC.fieldOf("size").forGetter(LongFeatureConfig::size),
            BlockStateProvider.CODEC.fieldOf("body").forGetter(LongFeatureConfig::body),
            BlockStateProvider.CODEC.fieldOf("top").forGetter(LongFeatureConfig::top),
            BlockStateProvider.CODEC.fieldOf("shell").forGetter(LongFeatureConfig::shell),
            Codec.FLOAT.fieldOf("top_chance").forGetter(LongFeatureConfig::topChance),
            Codec.FLOAT.fieldOf("shell_chance").forGetter(LongFeatureConfig::shellChance),
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("floor").forGetter(LongFeatureConfig::validFloor)
    ).apply(instance, LongFeatureConfig::new));
}
