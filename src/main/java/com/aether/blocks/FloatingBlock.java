package com.aether.blocks;

import com.aether.entities.block.FloatingBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

import java.util.Random;

@SuppressWarnings("deprecation")
public class FloatingBlock extends OreBlock {
    private final boolean powered;

    public FloatingBlock(boolean powered, AbstractBlock.Settings properties, UniformIntProvider experienceDropped) {
        super(properties, experienceDropped);
        this.powered = powered;
    }

    public FloatingBlock(boolean powered, AbstractBlock.Settings properties) {
        this(powered, properties, UniformIntProvider.create(0, 0));
    }

    public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
    }

    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(World worldIn, BlockPos pos) {
        if ((worldIn.isAir(pos.up()) || canFallThrough(worldIn.getBlockState(pos.up()))) && (!this.powered || worldIn.isReceivingRedstonePower(pos))) {
            if (!worldIn.isClient) {
                FloatingBlockEntity floatingblockentity = new FloatingBlockEntity(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, worldIn.getBlockState(pos));
                //this.onStartFloating(floatingblockentity);
                worldIn.spawnEntity(floatingblockentity);
            }
        }
    }

//    protected void onStartFloating(FloatingBlockEntity entityIn) {
//        entityIn.dropItem = false;
//    }

    public void onEndFloating(World worldIn, BlockPos posIn, BlockState floatingState, BlockState hitState) {
    }

    public void onBroken(World worldIn, BlockPos pos) {
    }

    protected int getFallDelay() {
        return 2;
    }

    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
    }

    public static boolean canFallThrough(BlockState state) {
        Material material = state.getMaterial();
        return state.isAir() || state.isIn(BlockTags.FIRE) || material.isLiquid() || material.isReplaceable();
    }

    @Environment(EnvType.CLIENT)
    public int getColor(BlockState state, BlockView world, BlockPos pos) {
        return -16777216;
    }
}