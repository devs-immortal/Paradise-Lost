package com.aether.blocks.natural;

import com.aether.blocks.AetherBlocks;
import com.aether.items.AetherItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.PlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class BlueberryBushBlock extends PlantBlock {

    public static final BooleanProperty RIPE = BooleanProperty.of("ripe");

    public BlueberryBushBlock(Settings settings) {
        super(settings);
        setDefaultState(this.getDefaultState().with(RIPE, false));
    }

    @Override
    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(RIPE);
        super.appendProperties(builder);
    }

    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (state.get(RIPE) && entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(0.800000011920929D, 0.75D, 0.800000011920929D));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(state.get(RIPE)) {
            world.spawnEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, new ItemStack(AetherItems.BLUE_BERRY, world.getRandom().nextInt(2) + 1)));
            world.setBlockState(pos, state.with(RIPE, false));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return !state.get(RIPE);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(world.getLightLevel(pos) >= 8 && world.getRandom().nextInt(world.getBlockState(pos.down()).isOf(AetherBlocks.AETHER_ENCHANTED_GRASS) ? 12 : 26) == 0) {
            world.setBlockState(pos, state.with(RIPE, true));
        }
    }

    @Override
    protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
        return floor.isOf(AetherBlocks.AETHER_GRASS) || floor.isOf(AetherBlocks.AETHER_ENCHANTED_GRASS) || floor.isOf(AetherBlocks.AETHER_FARMLAND) || floor.isOf(AetherBlocks.AETHER_DIRT);
    }

    @Override
    public float getAmbientOcclusionLightLevel(BlockState state, BlockView world, BlockPos pos) {
        return state.get(RIPE) ? 0.2F : 1.0F;
    }
}
