package net.id.aether.scheduler;

import java.util.HashMap;
import java.util.Map;

// this has never been used
public class AetherScheduler {
    public static final HashMap<Integer, ScheduleTask> tasks = new HashMap<>();

    public static void remove(int id) {
        try {
            tasks.remove(id);
        } catch (Exception ignored) {

        }
    }

    public static void add(ScheduleTask task) {
        tasks.put(tasks.size() + 1, task);
    }

    public static void pause(int id) {
        try {
            tasks.get(id).paused = true;
        } catch (Exception ignored) {

        }
    }

    public static void tick() {
        for (Map.Entry<Integer, ScheduleTask> entry : tasks.entrySet()) {
            ScheduleTask scheduleTask = entry.getValue();
            if (scheduleTask.getPaused()) continue;
            scheduleTask.currentTick++;
            if (scheduleTask.getCurrentTick() == scheduleTask.getDelay() || scheduleTask.isEveryTick()) {
                scheduleTask.execute();
                scheduleTask.currentTick = 0;
                return;
            }
        }
    }
}