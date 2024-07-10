package net.id.paradiselost.blocks;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.id.paradiselost.util.RenderUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;

import java.util.function.Consumer;

class ParadiseLostBlockActions {
    protected static final AbstractBlock.ContextPredicate never = (state, view, pos) -> false;
    protected static final AbstractBlock.ContextPredicate always = (state, view, pos) -> true;

    protected static Consumer<Block> flammable(int spread, int burn) {
        return (block) -> FlammableBlockRegistry.getDefaultInstance().add(block, spread, burn);
    }

    protected static final Consumer<Block> flammableLog = flammable(5, 5);
    protected static final Consumer<Block> flammablePlanks = flammable(20, 5);
    protected static final Consumer<Block> flammableLeaves = flammable(60, 30);
    protected static final Consumer<Block> flammablePlant = flammable(60, 100);

    protected static final Consumer<Block> translucentRenderLayer = (block) -> RenderUtils.transparentRenderLayer(block);
    protected static final Consumer<Block> cutoutRenderLayer = (block) -> RenderUtils.cutoutRenderLayer(block);
    protected static final Consumer<Block> cutoutMippedRenderLayer = (block) -> RenderUtils.cutoutMippedRenderLayer(block); //TODO

    protected static Consumer<Block> stripsTo(Block stripped) {
        return (original) -> StrippableBlockRegistry.register(original, stripped);
    }

    protected static Consumer<Block> tillable() {
        return (block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.FARMLAND.getDefaultState());
    }

    protected static Consumer<Block> coarseTillable() {
        return (block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.DIRT.getDefaultState());
    }

    protected static Consumer<Block> flattenable(Block turnInto) {
        return (block) -> FlattenableBlockRegistry.register(block, turnInto.getDefaultState());
    }
}
