package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Map;

public class AetherHoe extends HoeItem implements IAetherTool {

    protected static final Map<Block, BlockState> convertibleBlocks = Maps.newHashMap(ImmutableMap.of(
            Blocks.GRASS_BLOCK, Blocks.FARMLAND.defaultBlockState(),
            Blocks.DIRT_PATH, Blocks.FARMLAND.defaultBlockState(),
            Blocks.DIRT, Blocks.FARMLAND.defaultBlockState(),
            Blocks.COARSE_DIRT, Blocks.DIRT.defaultBlockState()
    ));
    private final AetherTiers material;

    public AetherHoe(AetherTiers material, Properties settings, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, settings);
        this.material = material;
        setupConvertibleData();
    }

    private void setupConvertibleData() {
        final Map<Block, BlockState> modifiedConvertibles = Maps.newHashMap(ImmutableMap.of(
                AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_FARMLAND.defaultBlockState(),
                AetherBlocks.AETHER_DIRT_PATH, AetherBlocks.AETHER_FARMLAND.defaultBlockState(),
                AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_FARMLAND.defaultBlockState()
        ));
        convertibleBlocks.putAll(modifiedConvertibles);
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        float original = super.getDestroySpeed(stack, state);
        if (this.getItemMaterial() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamageValue() / 50;
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionResult superUsage = super.useOn(context);
        if (superUsage.equals(InteractionResult.PASS)) {
            if (this.getItemMaterial() == AetherTiers.Gravitite && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
                return InteractionResult.SUCCESS;
            }
        }
        return superUsage;
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}