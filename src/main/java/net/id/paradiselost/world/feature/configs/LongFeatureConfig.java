package net.id.paradiselost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryCodecs;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.util.math.intprovider.IntProvider;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public record LongFeatureConfig(IntProvider size, BlockStateProvider body, BlockStateProvider top, BlockStateProvider shell, float topChance, float shellChance, RegistryEntryList<Block> validFloor) implements FeatureConfig {
    public static final Codec<LongFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            IntProvider.VALUE_CODEC.fieldOf("size").forGetter(LongFeatureConfig::size),
            BlockStateProvider.TYPE_CODEC.fieldOf("body").forGetter(LongFeatureConfig::body),
            BlockStateProvider.TYPE_CODEC.fieldOf("top").forGetter(LongFeatureConfig::top),
            BlockStateProvider.TYPE_CODEC.fieldOf("shell").forGetter(LongFeatureConfig::shell),
            Codec.FLOAT.fieldOf("top_chance").forGetter(LongFeatureConfig::topChance),
            Codec.FLOAT.fieldOf("shell_chance").forGetter(LongFeatureConfig::shellChance),
            RegistryCodecs.entryList(RegistryKeys.BLOCK).fieldOf("floor").forGetter(LongFeatureConfig::validFloor)
    ).apply(instance, LongFeatureConfig::new));
}
