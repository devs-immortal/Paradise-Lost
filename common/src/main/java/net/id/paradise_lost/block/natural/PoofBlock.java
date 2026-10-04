package net.id.paradise_lost.block.natural;

import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.LevelReader;
import net.id.paradise_lost.util.ParadiseLostSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import java.util.List;

public class PoofBlock extends Block {
    public PoofBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BlockState updateShape(BlockState state, LevelReader world, ScheduledTickAccess tickAccess, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        if (neighborState.isAir()) {
            if (world instanceof Level level) {
                for (int i = 0; i < 4; i++) {
                    level.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, pos.getX(), pos.getY(), pos.getZ(), random.nextFloat() * 0.5, random.nextFloat() * 0.5, random.nextFloat() * 0.5);
                }
                if (random.nextBoolean()) level.playSound(null, pos, ParadiseLostSoundEvents.BLOCK_SURTRUM_RUSH, SoundSource.BLOCKS, 0.1F + random.nextFloat() * 0.2F, random.nextFloat() * 0.7F + 0.3F);
                List<Entity> entities = level.getEntitiesOfClass(Entity.class, new AABB(pos.getX() - 5, pos.getY() - 5, pos.getZ() - 5, pos.getX() + 5, pos.getY() + 5, pos.getZ() + 5));
                for (Entity p : entities) {
                    if (p != null && p.isAlive() && p.blockPosition().closerThan(pos, 5)) p.setRemainingFireTicks(60);
                }
            }
            return Blocks.AIR.defaultBlockState();
        }
        return super.updateShape(state, world, tickAccess, pos, direction, neighborPos, neighborState, random);
    }

    public void animateTick(BlockState state, Level world, BlockPos pos, RandomSource random) {
        if (random.nextInt(48) == 0) {
            world.playLocalSound((double) pos.getX() + 0.5, (double) pos.getY() + 0.5, (double) pos.getZ() + 0.5, ParadiseLostSoundEvents.BLOCK_SURTRUM_CRACKLE, SoundSource.BLOCKS, 1.0F + random.nextFloat(), random.nextFloat() * 0.7F + 0.3F, false);
        }
    }

    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
