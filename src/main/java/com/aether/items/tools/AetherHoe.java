package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.entities.block.FloatingBlockEntity;
import com.aether.items.AetherItemGroups;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;

public class AetherHoe extends HoeItem implements IAetherTool {

    protected static final Map<Block, BlockState> convertibleBlocks = Maps.newHashMap(ImmutableMap.of(
            Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(),
            Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(),
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
        if (this.getItemMaterial() == AetherTiers.Zanite) return original + this.calculateIncrease(stack);
        return original;
    }

    private float calculateIncrease(ItemStack tool) {
        return (float) tool.getMaxDamage() / tool.getDamage() / 50;
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();
        if (this.getItemMaterial() == AetherTiers.Gravitite && FloatingBlockEntity.gravititeToolUsedOnBlock(context, this)) {
            return ActionResult.SUCCESS;
        }
        return super.useOnBlock(context);
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}
