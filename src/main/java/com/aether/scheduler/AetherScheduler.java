package com.aether.scheduler;

import java.util.HashMap;
import java.util.Map;

public class AetherScheduler {
    public static HashMap<Integer, ScheduleTask> Tasks = new HashMap<>();

    public static void Remove(int id) {
        try {
            Tasks.remove(id);
        } catch (Exception any) {

        }
    }

    public static void Add(ScheduleTask task) {
        Tasks.put(Tasks.size() + 1, task);
    }

    public static void Pause(int id) {
        try {
            Tasks.get(id).Paused = true;
        } catch (Exception any) {

        }
    }

    public static void Tick() {
        for (Map.Entry<Integer, ScheduleTask> entry : Tasks.entrySet()) {
            ScheduleTask scheduleTask = entry.getValue();
            if (scheduleTask.getPaused()) continue;
            scheduleTask.CurrentTick++;
            if (scheduleTask.getCurrentTick() == scheduleTask.getDelay() || scheduleTask.isEveryTick()) {
                scheduleTask.Execute();
                scheduleTask.CurrentTick = 0;
                return;
            }
        }
    }
}
