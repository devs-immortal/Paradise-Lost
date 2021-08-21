package com.aether.blocks;

import com.aether.entities.block.FloatingBlockHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.UniformIntProvider;
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

    @Override
    public void onBlockAdded(BlockState state, World worldIn, BlockPos posIn, BlockState oldState, boolean notify) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState stateIn, Direction facingIn, BlockState facingState, WorldAccess worldIn, BlockPos posIn, BlockPos facingPosIn) {
        worldIn.getBlockTickScheduler().schedule(posIn, this, this.getFallDelay());
        return super.getStateForNeighborUpdate(stateIn, facingIn, facingState, worldIn, posIn, facingPosIn);
    }

    @Override
    public void scheduledTick(BlockState stateIn, ServerWorld worldIn, BlockPos posIn, Random randIn) {
        this.checkFloatable(worldIn, posIn);
    }

    private void checkFloatable(World worldIn, BlockPos pos) {
        if (!this.powered || worldIn.isReceivingRedstonePower(pos)) {
            if (!worldIn.isClient) {
                BlockState state = worldIn.getBlockState(pos);
                if (state.isOf(AetherBlocks.GRAVITITE_LEVITATOR)){
                    FloatingBlockHelper.tryCreatePusher(worldIn, pos);
                } else {
                    FloatingBlockHelper.tryCreateGeneric(worldIn, pos);
                }
            }
        }
    }

    protected int getFallDelay() {
        return 2;
    }
}