package net.id.aether.world.feature.config;

import net.id.aether.blocks.AetherBlocks;
import net.minecraft.block.BlockState;

import java.util.Optional;

public class QuicksoilConfig extends DynamicConfiguration {
    private final Optional<BlockState> optionalState;

    public QuicksoilConfig(Optional<BlockState> state, Optional<String> type) {
        super(state.orElse(AetherBlocks.QUICKSOIL.getDefaultState()), type);
        this.optionalState = state;
    }

    public Optional<BlockState> getOptionalState() {
        return optionalState;
    }
}
