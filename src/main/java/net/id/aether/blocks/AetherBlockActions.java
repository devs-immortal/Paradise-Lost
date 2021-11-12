package net.id.aether.blocks;

import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.id.aether.util.RenderUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.HoeItem;
import net.minecraft.util.Identifier;

import static net.id.incubus_core.util.RegistryQueue.Action;
import static net.id.incubus_core.util.RegistryQueue.onClient;

class AetherBlockActions {
    protected static final AbstractBlock.ContextPredicate never = (state, view, pos) -> false;
    protected static final AbstractBlock.ContextPredicate always = (state, view, pos) -> true;

    protected static Action<Block> flammable(int spread, int burn) { return (id, block) -> FlammableBlockRegistry.getDefaultInstance().add(block, spread, burn);}
    protected static final Action<Block> flammableLog = flammable(5, 5);
    protected static final Action<Block> flammablePlanks = flammable(20, 5);
    protected static final Action<Block> flammableLeaves = flammable(60, 30);
    protected static final Action<Block> flammablePlant = flammable(60, 100);

    protected static final Action<Block> translucentRenderLayer = onClient((id, block) -> RenderUtils.transparentRenderLayer(block));
    protected static final Action<Block> cutoutRenderLayer = onClient((id, block) -> RenderUtils.cutoutRenderLayer(block));
    protected static final Action<Block> cutoutMippedRenderLayer = onClient((id, block) -> RenderUtils.cutoutMippedRenderLayer(block));
    protected static final Action<Block> auralRenderLayer = onClient((id, block) -> RenderUtils.auralRenderLayer(block));
    protected static final Action<Block> auralCutoutMippedRenderLayer = onClient((id, block) -> RenderUtils.auralCutoutMippedRenderLayer(block));
    protected static Action<Block> cubemapRenderLayer(Identifier texture){ return onClient((id, block) -> RenderUtils.cubemapRenderLayer(block, texture)); }
    
    protected static final Action<AbstractSignBlock> signBlockEntity = (id, block) -> ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks().add(block);

    protected static Action<Block> strippedFrom(Block original) { return (id, stripped) -> StrippableBlockRegistry.register(original, stripped);}
    protected static Action<Block> tillable() { return (id, block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, AetherBlocks.AETHER_FARMLAND.getDefaultState());}
    protected static Action<Block> coarseTillable() { return (id, block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, AetherBlocks.AETHER_DIRT.getDefaultState());}
    protected static Action<Block> flattenable() { return (id, block) -> FlattenableBlockRegistry.register(block, AetherBlocks.AETHER_DIRT_PATH.getDefaultState());}
}
