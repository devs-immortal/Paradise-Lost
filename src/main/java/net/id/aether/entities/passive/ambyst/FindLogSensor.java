package net.id.aether.entities.passive.ambyst;

import com.google.common.collect.ImmutableSet;
import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;
import java.util.Set;

public class FindLogSensor extends Sensor<AnimalEntity> {
    private final int range;

    public FindLogSensor(int range, int updateInterval) {
        super(updateInterval);
        this.range = range;
    }

    protected void sense(ServerWorld world, AnimalEntity entity) {
        Brain<?> brain = entity.getBrain();
        Optional<BlockPos> previousOpenPos = brain.getOptionalMemory(AetherEntityTypes.LOG_OPENING_MEMORY);
        Optional<BlockPos> previousLogPos = brain.getOptionalMemory(AetherEntityTypes.LOG_MEMORY);

        if (previousOpenPos.isPresent() && isValidOpening(world, previousOpenPos.get()) &&
                previousLogPos.isPresent() && isLog(world, previousLogPos.get()))
            return;

        for (BlockPos testLogPos : BlockPos.iterateOutwards(entity.getBlockPos(), this.range, this.range, this.range)) {
            if (isLog(world, testLogPos)) {
                BlockState logState = world.getBlockState(testLogPos);
                Direction logDir = Direction.from(logState.get(PillarBlock.AXIS), Direction.AxisDirection.POSITIVE);

                BlockPos foundLogPos;
                BlockPos openingPos = getLogOpening(world, logDir, testLogPos);
                if (openingPos == null) {
                    logDir = logDir.getOpposite();
                    openingPos = getLogOpening(world, logDir, testLogPos);
                }

                if (openingPos != null) {
                    foundLogPos = openingPos.offset(logDir.getOpposite());
                    entity.getBrain().remember(AetherEntityTypes.LOG_MEMORY, foundLogPos);
                    entity.getBrain().remember(AetherEntityTypes.LOG_OPENING_MEMORY, openingPos);
                    break;
                }
            }
        }
    }

    public BlockPos getLogOpening(ServerWorld world, Direction dir, BlockPos pos) {
        BlockPos newPos = pos.offset(dir);
        if (isLogSameRotation(world, newPos, dir)) {
            return getLogOpening(world, dir, newPos);
        }
        if (isValidOpening(world, newPos))
            return newPos;
        return null;
    }

    public static boolean isLog(ServerWorld world, BlockPos testPos) {
        return world.getBlockState(testPos).isOf(AetherBlocks.MOTTLED_SKYROOT_FALLEN_LOG) && world.getBlockState(testPos).get(PillarBlock.AXIS) != Direction.Axis.Y;
    }

    public static boolean isLogSameRotation(ServerWorld world, BlockPos testPos,Direction dir) {
        return world.getBlockState(testPos).isOf(AetherBlocks.MOTTLED_SKYROOT_FALLEN_LOG) && world.getBlockState(testPos).get(PillarBlock.AXIS) == dir.getAxis();
    }

    public static boolean isValidOpening(ServerWorld world, BlockPos testPos) {
        return !world.getBlockState(testPos).getMaterial().isSolid() && !world.getBlockState(testPos).isSolidBlock(world, testPos);
    }

    public Set<MemoryModuleType<?>> getOutputMemoryModules() {
        return ImmutableSet.of(AetherEntityTypes.LOG_MEMORY, AetherEntityTypes.LOG_OPENING_MEMORY);
    }
}
