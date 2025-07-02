package net.id.paradiselost.blocks;

import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.id.paradiselost.util.RenderUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.HoeItem;

import java.util.function.Consumer;

class ParadiseLostBlockActions {
    private static final boolean IS_CLIENT = FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    protected static final AbstractBlock.ContextPredicate never = (state, view, pos) -> false;
    protected static final AbstractBlock.ContextPredicate always = (state, view, pos) -> true;

    protected static Consumer<Block> flammable(int spread, int burn) {
        return block -> FlammableBlockRegistry.getDefaultInstance().add(block, spread, burn);
    }

    protected static final Consumer<Block> flammableLog = flammable(5, 5);
    protected static final Consumer<Block> flammablePlanks = flammable(20, 5);
    protected static final Consumer<Block> flammableLeaves = flammable(60, 30);
    protected static final Consumer<Block> flammablePlant = flammable(60, 100);

    protected static final Consumer<Block> translucentRenderLayer = clientOnly(RenderUtils::transparentRenderLayer);
    protected static final Consumer<Block> cutoutRenderLayer = clientOnly(RenderUtils::cutoutRenderLayer);
    protected static final Consumer<Block> cutoutMippedRenderLayer = clientOnly(RenderUtils::cutoutMippedRenderLayer);

    protected static Consumer<Block> stripsTo(Block stripped) {
        return original -> StrippableBlockRegistry.register(original, stripped);
    }

    protected static Consumer<Block> tillable() {
        return block -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.FARMLAND.getDefaultState());
    }

    protected static Consumer<Block> coarseTillable() {
        return block -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, ParadiseLostBlocks.DIRT.getDefaultState());
    }

    protected static Consumer<Block> flattenable(Block turnInto) {
        return block -> FlattenableBlockRegistry.register(block, turnInto.getDefaultState());
    }

    protected static Consumer<Block> clientOnly(Consumer<Block> func) {
        if (IS_CLIENT) return func;
        return block -> { };
    }
}
