package net.id.paradiselost.blocks.natural;

import net.id.paradiselost.util.ParadiseLostSoundEvents;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

import java.util.List;

public class PoofBlock extends Block {
    public PoofBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, Random random) {
        if (neighborState.isAir()) {
            if (world instanceof World realWorld) {
                for (int i = 0; i < 4; i++) {
                    realWorld.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY(), pos.getZ(), random.nextFloat() * 0.5, random.nextFloat() * 0.5, random.nextFloat() * 0.5);
                }
                if (random.nextBoolean())
                    realWorld.playSound(null, pos, ParadiseLostSoundEvents.BLOCK_SURTRUM_RUSH, SoundCategory.BLOCKS, 0.1F + random.nextFloat() * 0.2F, random.nextFloat() * 0.7F + 0.3F);
                List<Entity> entities = realWorld.getNonSpectatingEntities(Entity.class, new Box(pos.getX() - 5d, pos.getY() - 5d, pos.getZ() - 5d, pos.getX() + 5d, pos.getY() + 5d, pos.getZ() + 5d));
                for (Entity p : entities) {
                    if (p != null && p.isAlive() && p.getBlockPos().isWithinDistance(pos, 5)) p.setFireTicks(60);
                }
            }
            return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (random.nextInt(48) == 0) {
            world.playSound(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, ParadiseLostSoundEvents.BLOCK_SURTRUM_CRACKLE, SoundCategory.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.INVISIBLE;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.empty();
    }
}
