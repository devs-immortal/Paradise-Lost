package net.id.paradiselost.world.feature.configs;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.id.paradiselost.blocks.ParadiseLostBlocks;
import net.minecraft.block.BlockState;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class QuicksoilConfig extends DynamicConfiguration {
    public static final Codec<QuicksoilConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.optionalFieldOf("state").forGetter(QuicksoilConfig::getOptionalState),
            Codec.STRING.optionalFieldOf("genType").forGetter(QuicksoilConfig::getGenString)
    ).apply(instance, QuicksoilConfig::new));

    private final Optional<BlockState> optionalState;

    public QuicksoilConfig(Optional<BlockState> state, Optional<String> type) {
        super(state.orElse(ParadiseLostBlocks.QUICKSOIL.getDefaultState()), type);
        this.optionalState = state;
    }

    public QuicksoilConfig() {
        this(Optional.empty(), Optional.empty());
    }

    public Optional<BlockState> getOptionalState() {
        return optionalState;
    }
}
