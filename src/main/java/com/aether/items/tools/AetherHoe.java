package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

import java.util.Map;

public class AetherHoe extends HoeItem implements IAetherTool {

    protected static final Map<Block, BlockState> convertibleBlocks = Maps.newHashMap(ImmutableMap.of(
            Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(),
            Blocks.DIRT_PATH, Blocks.FARMLAND.getDefaultState(),
            Blocks.DIRT, Blocks.FARMLAND.getDefaultState(),
            Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState()
    ));
    private final AetherTiers material;

    public AetherHoe(AetherTiers material, Settings settings, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, settings);
        this.material = material;
        setupConvertibleData();
    }

    private void setupConvertibleData() {
        final Map<Block, BlockState> modifiedConvertibles = Maps.newHashMap(ImmutableMap.of(
                AetherBlocks.AETHER_GRASS_BLOCK, AetherBlocks.AETHER_FARMLAND.getDefaultState(),
                AetherBlocks.AETHER_DIRT_PATH, AetherBlocks.AETHER_FARMLAND.getDefaultState(),
                AetherBlocks.AETHER_DIRT, AetherBlocks.AETHER_FARMLAND.getDefaultState()
        ));
        convertibleBlocks.putAll(modifiedConvertibles);
    }

    @Override
    public float getMiningSpeedMultiplier(ItemStack stack, BlockState state) {
        float original = super.getMiningSpeedMultiplier(stack, state);
        if (this.getTier() == AetherTiers.ZANITE) return original + this.calculateIncrease(stack);
        return original;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ActionResult defaultResult = super.useOnBlock(context);
        return defaultResult != ActionResult.PASS ? defaultResult : IAetherTool.super.useOnBlock(context, defaultResult);
    }

    @Override
    public AetherTiers getTier() {
        return this.material;
    }

    @Override
    public ActionResult useOnEntity(ItemStack stack, PlayerEntity player, LivingEntity entity, Hand hand){
        return IAetherTool.super.useOnEntity(stack, player, entity, hand);
    }
}