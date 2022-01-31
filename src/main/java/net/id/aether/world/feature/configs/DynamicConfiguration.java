package net.id.aether.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.FeatureConfig;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class DynamicConfiguration implements FeatureConfig {
    public static final Codec<DynamicConfiguration> CODEC = RecordCodecBuilder.create(instance->instance.group(
            BlockState.CODEC.fieldOf("state").forGetter(DynamicConfiguration::getState),
            Codec.STRING.optionalFieldOf("genType").forGetter(DynamicConfiguration::getGenString)
    ).apply(instance, DynamicConfiguration::new));

    public final BlockState state;
    public final Optional<String> genTypeRaw;
    public final GeneratorType genType;

    public DynamicConfiguration(BlockState state, Optional<String> type) {
        this.state = state;
        this.genTypeRaw = type;

        final String genString = (genTypeRaw.orElse("normal")).toUpperCase();
        GeneratorType genType = GeneratorType.NORMAL;
        try {
            genType = GeneratorType.valueOf(genString);
        } catch (Exception ignored) {
        }
        this.genType = genType;
    }

    public BlockState getState() {
        return this.state;
    }

    public Optional<String> getGenString() {
        return genTypeRaw;
    }

    public GeneratorType getGenType() {
        return genType;
    }

    public enum GeneratorType {
        LEGACY, NORMAL
    }
}
