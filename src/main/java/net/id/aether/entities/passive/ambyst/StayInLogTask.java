package net.id.aether.entities.passive.ambyst;

import com.google.common.collect.ImmutableMap;
import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleState;
import net.minecraft.entity.ai.brain.task.Task;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class StayInLogTask extends Task<AnimalEntity> {
    public StayInLogTask() {
        super(ImmutableMap.of(AetherEntityTypes.IS_RAINING_MEMORY, MemoryModuleState.VALUE_PRESENT,
                AetherEntityTypes.LOG_MEMORY, MemoryModuleState.VALUE_PRESENT,
                AetherEntityTypes.LOG_OPENING_MEMORY, MemoryModuleState.VALUE_PRESENT));
    }

    protected boolean shouldRun(ServerWorld world, AnimalEntity entity) {
        if (FindLogSensor.isLog(world, entity.getBlockPos()))
            return false;

        Brain<?> brain = entity.getBrain();
        Boolean isRaining = brain.getOptionalMemory(AetherEntityTypes.IS_RAINING_MEMORY).get();
        if (isRaining)
            return false;

        BlockPos openPos = brain.getOptionalMemory(AetherEntityTypes.LOG_OPENING_MEMORY).get();
        if (!FindLogSensor.isValidOpening(world, openPos))
            return false;

        BlockPos logPos = brain.getOptionalMemory(AetherEntityTypes.LOG_MEMORY).get();
        return FindLogSensor.isLog(world, logPos);
    }

    @Override
    protected boolean shouldKeepRunning(ServerWorld world, AnimalEntity entity, long time) {
        return shouldRun(world, entity);
    }

    protected void run(ServerWorld world, AnimalEntity entity, long time) {
        Brain<?> brain = entity.getBrain();

        BlockPos openPos = brain.getOptionalMemory(AetherEntityTypes.LOG_OPENING_MEMORY).get();
        BlockPos logPos = brain.getOptionalMemory(AetherEntityTypes.LOG_MEMORY).get();

        System.out.println("crawling in hole");

    }
}
