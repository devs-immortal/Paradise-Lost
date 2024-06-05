package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.id.paradiselost.util.RenderUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;

class ParadiseLostBlockActions {
    protected static final AbstractBlock.ContextPredicate never = (state, view, pos) -> false;
    protected static final AbstractBlock.ContextPredicate always = (state, view, pos) -> true;

    protected static Action<Block> flammable(int spread, int burn) {
        return (id, block) -> FlammableBlockRegistry.getDefaultInstance().add(block, spread, burn);
    }

    protected static final Action<Block> flammableLog = flammable(5, 5);
    protected static final Action<Block> flammablePlanks = flammable(20, 5);
    protected static final Action<Block> flammableLeaves = flammable(60, 30);
    protected static final Action<Block> flammablePlant = flammable(60, 100);

    protected static final Action<Block> translucentRenderLayer = onClient((id, block) -> RenderUtils.transparentRenderLayer(block));
    protected static final Action<Block> cutoutRenderLayer = onClient((id, block) -> RenderUtils.cutoutRenderLayer(block));
    protected static final Action<Block> cutoutMippedRenderLayer = onClient((id, block) -> RenderUtils.cutoutMippedRenderLayer(block));

    protected static Action<Block> stripsTo(Block stripped) {
        return (id, original) -> StrippableBlockRegistry.register(original, stripped);
    }

    protected static Action<Block> tillable() {
        return (id, block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.FARMLAND.getDefaultState());
    }

    protected static Action<Block> coarseTillable() {
        return (id, block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.DIRT.getDefaultState());
    }

    protected static Action<Block> flattenable(Block turnInto) {
        return (id, block) -> FlattenableBlockRegistry.register(block, turnInto.getDefaultState());
    }
}
