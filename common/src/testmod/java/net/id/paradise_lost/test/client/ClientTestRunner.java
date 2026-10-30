package net.id.paradise_lost.test.client;

import net.id.paradise_lost.test.Recorder;
import net.id.paradise_lost.test.Test;
import net.id.paradise_lost.test.TestRun;
import net.id.paradise_lost.test.TickEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.world.level.WorldDataConfiguration;

import java.util.List;
import java.util.function.Supplier;

import static net.id.paradise_lost.test.client.ClientTestHelpers.client;

public final class ClientTestRunner implements TestRun.Hooks {
    private static final int WORLD_SETTLE_TICKS = 40;
    private static final int RESET_SETTLE_TICKS = 20;
    private static final List<Class<?>> TEST_CLASSES = List.of(
            FoodTests.class,
            BoatTests.class,
            NitraTests.class,
            BlockTests.class,
            CompatTests.class,
            DimensionTests.class,
            EntityTests.class,
            FoodBowlTests.class,
            ItemTests.class,
            LevitaTests.class,
            MoaTests.class,
            PotTests.class,
            RegistryTests.class
    );

    public static String title;
    public static int currentTestNumber;

    private final Supplier<WorldDataConfiguration> dataConfiguration;
    private Phase phase = Phase.CREATE_WORLD;
    private TestRun run;

    private ClientTestRunner(Supplier<WorldDataConfiguration> dataConfiguration) {
        this.dataConfiguration = dataConfiguration;
    }

    public static void init(Supplier<WorldDataConfiguration> dataConfiguration) {
        ClientTestRunner runner = new ClientTestRunner(dataConfiguration);
        Recorder.register();
        TickEvents.CLIENT_END.add(runner::tick);
    }

    private void tick() {
        Minecraft client = client();
        switch (phase) {
            case CREATE_WORLD -> {
                if (client.getOverlay() != null) return;
                client.options.pauseOnLostFocus = false;
                TestWorld.create(dataConfiguration.get());
                phase = Phase.WAIT_FOR_WORLD;
            }
            case WAIT_FOR_WORLD -> {
                if (client.level == null || client.player == null || client.screen != null) return;
                run = new TestRun("ClientTest", TEST_CLASSES, WORLD_SETTLE_TICKS, this);
                phase = Phase.RUN;
            }
            case RUN -> {
                client.options.pauseOnLostFocus = false;
                if (client.screen instanceof PauseScreen) {
                    client.setScreen(null);
                }
                run.tick();
            }
        }
    }

    @Override
    public boolean ready() {
        return TestWorld.playerAlive();
    }

    @Override
    public int beforeTest(Test test, int index, int count) {
        currentTestNumber = index + 1;
        title = "[" + currentTestNumber + "/" + count + "] " + test.fullName();
        client().updateTitle();
        TestWorld.resetPlayer(index);
        return RESET_SETTLE_TICKS;
    }

    @Override
    public void finished() {
        client().execute(() -> client().stop());
    }

    private enum Phase {
        CREATE_WORLD, WAIT_FOR_WORLD, RUN
    }
}
