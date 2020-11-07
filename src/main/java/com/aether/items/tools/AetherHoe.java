package com.aether.items.tools;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItemGroups;
import com.aether.items.utils.AetherTiers;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
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

    public AetherHoe(AetherTiers material, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, new Settings().group(AetherItemGroups.Tools));
        this.material = material;
        setupConvertibleData();
    }

    public AetherHoe(AetherTiers material, Rarity rarity, float attackSpeed) {
        super(material.getDefaultTier(), 1, attackSpeed, new Settings().group(AetherItemGroups.Tools).rarity(rarity));
        this.material = material;
        setupConvertibleData();
    }

    private void setupConvertibleData() {
        final Map<Block, BlockState> modifiedConvertibles = Maps.newHashMap(ImmutableMap.of(
                AetherBlocks.aether_grass, AetherBlocks.aether_farmland.getDefaultState(),
                AetherBlocks.aether_grass_path, AetherBlocks.aether_farmland.getDefaultState(),
                AetherBlocks.aether_dirt, AetherBlocks.aether_farmland.getDefaultState()
        ));
        convertibleBlocks.putAll(modifiedConvertibles);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        if (context.getSide() != Direction.DOWN && world.isAir(blockPos.up())) {
            BlockState blockState = convertibleBlocks.get(world.getBlockState(blockPos).getBlock());

            if (blockState != null) {
                PlayerEntity entityPlayer = context.getPlayer();

                world.playSound(entityPlayer, blockPos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);

                if (!world.isClient) {
                    world.setBlockState(blockPos, blockState, 11);
                    if (entityPlayer != null) context.getStack().damage(1, entityPlayer, null);
                }
                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public AetherTiers getItemMaterial() {
        return this.material;
    }
}