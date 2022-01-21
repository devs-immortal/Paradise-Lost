package net.id.aether.world.feature.configs;

import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;

import java.util.Optional;

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class QuicksoilConfig extends DynamicConfiguration {
    private final Optional<BlockState> optionalState;

    public QuicksoilConfig(Optional<BlockState> state, Optional<String> type) {
        super(state.orElse(AetherBlocks.QUICKSOIL.getDefaultState()), type);
        this.optionalState = state;
    }

    public QuicksoilConfig() {
        this(Optional.empty(), Optional.empty());
    }

    public Optional<BlockState> getOptionalState() {
        return optionalState;
    }
}
