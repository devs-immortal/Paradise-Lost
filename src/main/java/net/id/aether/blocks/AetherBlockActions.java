package net.id.aether.blocks;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.registry.FlammableBlockRegistry;
import net.fabricmc.fabric.api.registry.FlattenableBlockRegistry;
import net.fabricmc.fabric.api.registry.StrippableBlockRegistry;
import net.fabricmc.fabric.api.registry.TillableBlockRegistry;
import net.fabricmc.fabric.mixin.lookup.BlockEntityTypeAccessor;
import net.id.incubus_core.util.RegistryQueue;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.item.HoeItem;

import static net.minecraft.block.AbstractBlock.Settings.copy;
import static net.minecraft.block.Blocks.POTTED_OAK_SAPLING;

class AetherBlockActions {
    protected static final AbstractBlock.ContextPredicate never = (state, view, pos) -> false;
    protected static final AbstractBlock.ContextPredicate always = (state, view, pos) -> true;

    protected static RegistryQueue.Action<Block> flammable(int spread, int burn) { return (id, block) -> FlammableBlockRegistry.getDefaultInstance().add(block, spread, burn);}
    protected static final RegistryQueue.Action<Block> flammableLog = flammable(5, 5);
    protected static final RegistryQueue.Action<Block> flammablePlanks = flammable(20, 5);
    protected static final RegistryQueue.Action<Block> flammableLeaves = flammable(60, 30);
    protected static final RegistryQueue.Action<Block> flammablePlant = flammable(60, 100);

    protected static RegistryQueue.Action<Block> renderLayer(RenderLayer layer) { return RegistryQueue.onClient((id, block) -> BlockRenderLayerMap.INSTANCE.putBlock(block, layer));}
    protected static final RegistryQueue.Action<Block> translucentRenderLayer = renderLayer(RenderLayer.getTranslucent());
    protected static final RegistryQueue.Action<Block> cutoutRenderLayer = renderLayer(RenderLayer.getCutout());
    protected static final RegistryQueue.Action<Block> cutoutMippedRenderLayer = renderLayer(RenderLayer.getCutoutMipped());

    protected static final RegistryQueue.Action<AbstractSignBlock> signBlockEntity = (id, block) -> ((BlockEntityTypeAccessor) BlockEntityType.SIGN).getBlocks().add(block);

    protected static RegistryQueue.Action<Block> strippedFrom(Block original) { return (id, stripped) -> StrippableBlockRegistry.register(original, stripped);}
    protected static RegistryQueue.Action<Block> tillable() { return (id, block) -> TillableBlockRegistry.register(block, HoeItem::canTillFarmland, AetherBlocks.AETHER_FARMLAND.getDefaultState());}
    protected static RegistryQueue.Action<Block> flattenable() { return (id, block) -> FlattenableBlockRegistry.register(block, AetherBlocks.AETHER_DIRT_PATH.getDefaultState());}
}
