package net.id.aether.scheduler;

public abstract class ScheduleTask {
    public int Delay;
    public int CurrentTick;
    public boolean Paused;
    public boolean EveryTick;

    public abstract void Execute();

    public int getDelay() {
        return Delay;
    }

    public boolean getPaused() {
        return Paused;
    }

    public int getCurrentTick() {
        return CurrentTick;
    }

    public boolean isEveryTick() {
        return EveryTick;
    }
}
