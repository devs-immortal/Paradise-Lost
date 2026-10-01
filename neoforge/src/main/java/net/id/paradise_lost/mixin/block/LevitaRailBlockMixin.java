package net.id.paradise_lost.mixin.block;

import net.id.paradise_lost.block.mechanical.LevitaRailBlock;
import net.minecraft.world.level.block.PoweredRailBlock;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(LevitaRailBlock.class)
public abstract class LevitaRailBlockMixin extends PoweredRailBlock {

    public LevitaRailBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isActivatorRail() {
        return false;
    }
}
