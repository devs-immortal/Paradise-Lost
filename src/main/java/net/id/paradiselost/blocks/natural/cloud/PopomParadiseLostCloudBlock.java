package net.id.paradiselost.blocks.natural.cloud;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PopomParadiseLostCloudBlock extends ParadiseLostCloudBlock {
    public static final DirectionProperty FACING = Properties.FACING;

    protected static VoxelShape SHAPE = VoxelShapes.empty();

    public PopomParadiseLostCloudBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.UP));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, BlockRotation rotation) {
        if (rotation == BlockRotation.COUNTERCLOCKWISE_90 || rotation == BlockRotation.CLOCKWISE_90) {
            return switch (state.get(FACING)) {
                case NORTH -> state.with(FACING, Direction.EAST);
                case EAST -> state.with(FACING, Direction.SOUTH);
                case SOUTH -> state.with(FACING, Direction.WEST);
                case WEST -> state.with(FACING, Direction.NORTH);
                default -> state;
            };
        } else {
            return state;
        }
    }
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction lookDirection = ctx.getPlayerLookDirection();
        Direction invertedDirection = lookDirection.getOpposite(); // Invert
        return getDefaultState().with(FACING, invertedDirection);
    }


    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3d motion = entity.getVelocity();
        Direction direction = state.get(FACING);

        // If crouching and going downwards, slow the fall
        if (entity.isSneaking()) {
            if (motion.y < 0) {
                entity.setVelocity(motion.multiply(1.0, 0.005, 1.0));
            }
            return;
        }

        if (motion.length() < 2) {
            // Launch direction based on block facing
            Vec3d launchVec = new Vec3d(
                    direction.getOffsetX(),
                    direction.getOffsetY(),
                    direction.getOffsetZ()
            ).normalize();

            double launchStrength = 2.0;
            Vec3d newVel = launchVec.multiply(launchStrength);
            entity.setVelocity(newVel);
        }


        if (world.isClient && !entity.verticalCollision && !(entity instanceof PlayerEntity player && player.isCreative())) {
            for (int count = 0; count < 50; count++) {
                double xOffset = pos.getX() + world.random.nextDouble();
                double yOffset = pos.getY() + world.random.nextDouble();
                double zOffset = pos.getZ() + world.random.nextDouble();

                world.addParticle(ParticleTypes.SPLASH, xOffset, yOffset, zOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

}
