package net.id.paradise_lost.test.server;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestRun;
import net.id.paradise_lost.test.TickEvents;
import net.minecraft.server.MinecraftServer;

import java.util.List;

public final class ServerTestRunner implements TestRun.Hooks {
    private static final int START_SETTLE_TICKS = 20;
    private static final List<Class<?>> TEST_CLASSES = List.of(
            DimensionTests.class
    );

    private static MinecraftServer server;

    private TestRun run;

    private ServerTestRunner() {
    }

    public static void init() {
        ServerTestRunner runner = new ServerTestRunner();
        Recorder.register();
        TickEvents.SERVER_END.add(runner::tick);
    }

    public static MinecraftServer server() {
        return server;
    }

    private void tick(MinecraftServer current) {
        if (run == null) {
            server = current;
            run = new TestRun("ServerTest", TEST_CLASSES, START_SETTLE_TICKS, this);
        }
        run.tick();
    }

    @Override
    public int beforeTest(Test test, int index, int count) {
        return 0;
    }

    @Override
    public void finished() {
        server.halt(false);
    }
}
