package net.id.aether.blocks.natural.aercloud;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.Random;

public class DirectionalAercloudBlock extends AercloudBlock {

    protected static VoxelShape SHAPE = VoxelShapes.empty();
    public static final EnumProperty<Direction> FACING = Properties.FACING;

    public DirectionalAercloudBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));

    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3d motion = entity.getVelocity();

        if (entity.isSneaking()) {
            if (motion.y < 0) {
                entity.setVelocity(motion.multiply(1.0, 0.005, 1.0));
            }
            return;
        }

        switch (state.get(FACING)) {
            case EAST -> entity.setVelocity(2.0, motion.y, motion.z);
            case WEST -> entity.setVelocity(-2.0, motion.y, motion.z);
            case UP -> entity.setVelocity(motion.x, 1.0, motion.z);
            case DOWN -> entity.setVelocity(motion.x, -1.0, motion.z);
            case SOUTH -> entity.setVelocity(motion.x, motion.y, 2.0);
            case NORTH -> entity.setVelocity(motion.x, motion.y, -2.0);
        }
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        super.randomDisplayTick(state, world, pos, random);
        final float x = pos.getX() + (random.nextFloat() * 0.7f) + 0.15f;
        final float y = pos.getY() + (random.nextFloat() * 0.7f) + 0.15f;
        final float z = pos.getZ() + (random.nextFloat() * 0.7f) + 0.15f;

        final Direction facing = state.get(FACING);

        final float motionX = facing.getOffsetX() * ((random.nextFloat() * 0.75f) + 0.45f);
        final float motionZ = facing.getOffsetZ() * ((random.nextFloat() * 0.75f) + 0.45f);
        final float motionY = facing.getOffsetY() * ((random.nextFloat() * 0.15f) + 0.25f);

        world.addParticle(ParticleTypes.CLOUD, x, y, z, motionX, motionY, motionZ);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getSide());
    }
}
