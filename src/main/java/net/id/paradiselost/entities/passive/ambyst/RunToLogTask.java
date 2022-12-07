package net.id.paradiselost.entities.passive.ambyst;

import com.google.common.collect.ImmutableMap;
import net.id.paradiselost.entities.ParadiseLostEntityTypes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNode;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class RunToLogTask extends Task<AnimalEntity> {
    private final float speed = .25f;

    public RunToLogTask() {
        super(ImmutableMap.of(ParadiseLostEntityTypes.IS_RAINING_MEMORY, MemoryModuleState.VALUE_PRESENT,
                ParadiseLostEntityTypes.LOG_MEMORY, MemoryModuleState.VALUE_PRESENT,
                ParadiseLostEntityTypes.LOG_OPENING_MEMORY, MemoryModuleState.VALUE_PRESENT));
    }

    protected boolean shouldRun(ServerWorld world, AnimalEntity entity) {
        if (FindLogSensor.isLog(world, entity.getBlockPos()))
            return false;

        Brain<?> brain = entity.getBrain();
        Boolean isRaining = brain.getOptionalMemory(ParadiseLostEntityTypes.IS_RAINING_MEMORY).get();
        if (isRaining)
            return false;

        BlockPos openPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_OPENING_MEMORY).get();
        if (!FindLogSensor.isValidOpening(world, openPos))
            return false;

        BlockPos logPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_MEMORY).get();
        return FindLogSensor.isLog(world, logPos);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, AnimalEntity entity, long time) {
        return shouldRun(world, entity);
    }

    protected void run(ServerWorld world, AnimalEntity entity, long time) {
        Brain<?> brain = entity.getBrain();

        BlockPos openPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_OPENING_MEMORY).get();
        BlockPos logPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_MEMORY).get();

        Path path = entity.getNavigation().findPathTo(openPos, 0);
        if (path != null) {
            path = extendPath(path, logPos);
            entity.getNavigation().startMovingAlong(path, speed);
            entity.getLookControl().lookAt(logPos.getX() + .5f, logPos.getY(), logPos.getZ() + .5f);
        }
    }

    @Override
    protected void keepRunning(ServerWorld world, AnimalEntity entity, long time) {
        Brain<?> brain = entity.getBrain();

        BlockPos openPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_OPENING_MEMORY).get();
        BlockPos logPos = brain.getOptionalMemory(ParadiseLostEntityTypes.LOG_MEMORY).get();
        entity.getLookControl().lookAt(openPos.getX() + .5f, openPos.getY(), openPos.getZ() + .5f);
    }

    public Path extendPath(Path path, BlockPos newNode) {
        List<PathNode> oldNodes = new ArrayList<>();
        for (int i = 0; i < path.getLength(); i++)
            oldNodes.add(path.getNode(i));
        PathNode finalNode = new PathNode(newNode.getX(), newNode.getY(), newNode.getZ());
        finalNode.previous = path.getEnd();

        oldNodes.add(path.getEnd());
        oldNodes.add(finalNode);
        return new Path(oldNodes, newNode, path.reachesTarget());
    }
}
