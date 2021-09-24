package net.id.aether.scheduler;

// this has never been used
public abstract class ScheduleTask {
    public int delay;
    public int currentTick;
    public boolean paused;
    public boolean everyTick;

    public abstract void execute();

    public int getDelay() {
        return delay;
    }

    public boolean getPaused() {
        return paused;
    }

    public int getCurrentTick() {
        return currentTick;
    }

    public boolean isEveryTick() {
        return everyTick;
    }
}
