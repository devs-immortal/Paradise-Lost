package net.id.paradise_lost.test.server;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestRun;
import net.id.paradise_lost.test.TickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;

import java.util.List;

public final class ServerTestRunner implements TestRun.Hooks {
    private static final int START_SETTLE_TICKS = 20;
    private static final int RESET_SETTLE_TICKS = 20;
    private static final int SITE_SPACING = 128;
    private static final int SITE_RADIUS = 64;
    private static final List<Class<?>> TEST_CLASSES = List.of(
            BlockTests.class,
            BoatTests.class,
            CreativeTabTests.class,
            DimensionTests.class,
            EntityTests.class,
            ItemTests.class,
            LevitaTests.class,
            MoaTests.class,
            NitraTests.class,
            PotTests.class,
            RegistryTests.class
    );

    private static MinecraftServer server;
    private static BlockPos site;

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

    public static BlockPos site() {
        return site;
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
        forceSiteChunks(false);
        site = new BlockPos(index * SITE_SPACING, -60, 0);
        forceSiteChunks(true);
        return RESET_SETTLE_TICKS;
    }

    private static void forceSiteChunks(boolean forced) {
        if (site == null) return;
        ServerLevel level = server.overworld();
        ChunkPos from = new ChunkPos(site.offset(-SITE_RADIUS, 0, -SITE_RADIUS));
        ChunkPos to = new ChunkPos(site.offset(SITE_RADIUS, 0, SITE_RADIUS));
        ChunkPos.rangeClosed(from, to).forEach(chunk -> level.setChunkForced(chunk.x, chunk.z, forced));
    }

    @Override
    public void finished() {
        server.halt(false);
    }
}
