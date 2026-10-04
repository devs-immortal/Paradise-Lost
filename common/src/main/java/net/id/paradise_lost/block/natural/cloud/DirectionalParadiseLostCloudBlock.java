package net.id.paradise_lost.block.natural.cloud;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DirectionalParadiseLostCloudBlock extends ParadiseLostCloudBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;

    protected static VoxelShape SHAPE = Shapes.empty();

    public DirectionalParadiseLostCloudBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.UP));
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return changeRotation(state, rotation);
    }

    public static BlockState changeRotation(BlockState state, Rotation rotation) {
        if (rotation == Rotation.COUNTERCLOCKWISE_90 || rotation == Rotation.CLOCKWISE_90) {
            return switch (state.getValue(FACING)) {
                case NORTH -> state.setValue(FACING, Direction.EAST);
                case EAST -> state.setValue(FACING, Direction.SOUTH);
                case SOUTH -> state.setValue(FACING, Direction.WEST);
                case WEST -> state.setValue(FACING, Direction.NORTH);
                default -> state;
            };
        } else {
            return state;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(new Property[]{FACING});
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction lookDirection = ctx.getNearestLookingDirection();
        Direction invertedDirection = lookDirection.getOpposite();
        return defaultBlockState().setValue(FACING, invertedDirection);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3 motion = entity.getDeltaMovement();
        Direction direction = state.getValue(FACING);

        if (entity.isShiftKeyDown()) {
            if (motion.y < 0) {
                entity.setDeltaMovement(motion.multiply(1.0, 0.005, 1.0));
            }
            return;
        }

        if (true) {
            Vec3 launchVec = new Vec3(
                    direction.getStepX(),
                    direction.getStepY(),
                    direction.getStepZ()
            ).normalize();

            double launchStrength = 2.2;
            double retainHorizontal = 0.2;
            double retainVertical = 0.25;
            double horizontalSpeedSq = motion.x * motion.x + motion.z * motion.z;
            double verticalSpeedSq = motion.y * motion.y;

            double newX = motion.x;
            double newZ = motion.z;
            if (horizontalSpeedSq < 5.0) {
                newX = motion.x * retainHorizontal + launchVec.x * launchStrength * (1.0 - retainHorizontal);
                newZ = motion.z * retainHorizontal + launchVec.z * launchStrength * (1.0 - retainHorizontal);
            }

            double newY = motion.y;
            if (verticalSpeedSq < 4.0) {
                newY = motion.y * retainVertical + launchVec.y * launchStrength * (1.0 - retainVertical);
            }

            entity.setDeltaMovement(newX, newY, newZ);
        }
        if (world.isClientSide && !entity.verticalCollision && !(entity instanceof Player player && player.isCreative())) {
            for (int count = 0; count < 50; count++) {
                double xOffset = pos.getX() + world.random.nextDouble();
                double yOffset = pos.getY() + world.random.nextDouble();
                double zOffset = pos.getZ() + world.random.nextDouble();

                world.addParticle(ParticleTypes.SPLASH, xOffset, yOffset, zOffset, 0.0, 0.0, 0.0);
            }
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

}
