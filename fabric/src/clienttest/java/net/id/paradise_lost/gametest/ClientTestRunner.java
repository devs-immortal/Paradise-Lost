package net.id.paradise_lost.gametest;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.id.paradise_lost.gametest.tests.BlockTests;
import net.id.paradise_lost.gametest.tests.BoatTests;
import net.id.paradise_lost.gametest.tests.CompatTests;
import net.id.paradise_lost.gametest.tests.DimensionTests;
import net.id.paradise_lost.gametest.tests.EntityTests;
import net.id.paradise_lost.gametest.tests.FoodBowlTests;
import net.id.paradise_lost.gametest.tests.FoodTests;
import net.id.paradise_lost.gametest.tests.ItemTests;
import net.id.paradise_lost.gametest.tests.LevitaTests;
import net.id.paradise_lost.gametest.tests.MoaTests;
import net.id.paradise_lost.gametest.tests.NitraTests;
import net.id.paradise_lost.gametest.tests.PotTests;
import net.id.paradise_lost.gametest.tests.RegistryTests;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionException;

import static net.id.paradise_lost.gametest.TestHelpers.client;

public class ClientTestRunner implements ClientModInitializer {
    private static final Logger LOG = LoggerFactory.getLogger("ClientTest");
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
    public static int currentTestCount;

    private Phase phase = Phase.CREATE_WORLD;
    private final Deque<Test> tests = new ArrayDeque<>();
    private final List<String> failures = new ArrayList<>();

    private Running running;
    private int testIndex;
    private int testCount;
    private int wait;

    @Override
    public void onInitializeClient() {
        Recorder.register();
        ClientTickEvents.END_CLIENT_TICK.register(client -> tick());
    }

    private void tick() {
        Minecraft client = client();
        switch (phase) {
            case CREATE_WORLD -> {
                if (client.getOverlay() != null) return;
                client.options.pauseOnLostFocus = false;
                TestWorld.create();
                phase = Phase.WAIT_FOR_WORLD;
            }
            case WAIT_FOR_WORLD -> {
                if (client.level == null || client.player == null || client.screen != null) return;
                loadTests();
                wait = WORLD_SETTLE_TICKS;
                phase = Phase.RUN;
            }
            case RUN -> runTests();
        }
    }

    private void loadTests() {
        TEST_CLASSES.forEach(type -> tests.addAll(TestLoader.load(type)));
        String only = System.getenv("RUN_ONLY");
        if (only != null) {
            String[] filters = Arrays.stream(only.split(";"))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .toArray(String[]::new);
            tests.removeIf(test -> Arrays.stream(filters).noneMatch(filter ->
                    test.group().equalsIgnoreCase(filter)
                            || test.fullName().equalsIgnoreCase(filter)
                            || test.name().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))));
        }
        testCount = tests.size();
    }

    private void runTests() {
        Minecraft client = client();
        client.options.pauseOnLostFocus = false;
        if (client.screen instanceof net.minecraft.client.gui.screens.PauseScreen) {
            client.setScreen(null);
        }
        if (wait > 0) {
            wait--;
        } else if (running == null) {
            startNextTest();
        } else {
            runStep();
        }
    }

    private void startNextTest() {
        if (!TestWorld.playerAlive()) return;
        Test test = tests.poll();
        if (test == null) {
            finish();
            return;
        }
        running = new Running(test, Recorder.logErrors.size());
        currentTestNumber = testIndex + 1;
        currentTestCount = testCount;
        setTitle("[" + currentTestNumber + "/" + testCount + "] " + test.fullName());
        try {
            TestWorld.resetPlayer(testIndex++);
        } catch (Throwable t) {
            fail(t);
            return;
        }
        wait = RESET_SETTLE_TICKS + running.currentStep().delay();
    }

    private void runStep() {
        try {
            boolean done = switch (running.currentStep()) {
                case Step.Run run -> {
                    run.action().run();
                    yield true;
                }
                case Step.Until until -> conditionMet(until);
            };
            if (!done) return;
            if (++running.step < running.test.steps().size()) {
                wait = running.currentStep().delay();
                if (wait == 0) {
                    runStep();
                }
                return;
            }
            checkNoNewLogErrors();
        } catch (Throwable t) {
            fail(t);
            return;
        }
        LOG.info("PASS {}", running.test.fullName());
        running = null;
    }

    private boolean conditionMet(Step.Until until) {
        if (until.condition().getAsBoolean()) {
            running.waited = 0;
            return true;
        }
        if (++running.waited >= until.timeout())
            throw new AssertionError("timed out waiting for " + until.what());
        return false;
    }

    private void checkNoNewLogErrors() {
        List<String> errors = Recorder.errorsSince(running.logErrorsAtStart);
        if (!errors.isEmpty())
            throw new AssertionError(errors.size() + " errors logged. first: " + errors.getFirst());
    }

    private void fail(Throwable t) {
        Throwable cause = t instanceof CompletionException && t.getCause() != null ? t.getCause() : t;
        String name = running.test.fullName();
        if (cause instanceof AssertionError) {
            LOG.error("FAIL {}: {}", name, cause.getMessage());
        } else {
            LOG.error("FAIL {}: {}", name, cause.getMessage(), cause);
        }
        failures.add(name + ": " + cause.getMessage());
        running = null;
    }

    private static void setTitle(String text) {
        title = text;
        client().updateTitle();
    }

    private void finish() {
        if (!Recorder.logWarnings.isEmpty()) {
            LOG.warn("{} warnings logged during the run:", Recorder.logWarnings.size());
            Recorder.logWarnings.forEach(w -> LOG.warn("  {}", w));
        }
        if (failures.isEmpty()) {
            LOG.info("All tests passed");
        } else {
            LOG.error("{} tests failed:", failures.size());
            failures.forEach(f -> LOG.error("  {}", f));
        }
        Minecraft.getInstance().execute(() -> Minecraft.getInstance().stop());
    }

    private enum Phase {
        CREATE_WORLD, WAIT_FOR_WORLD, RUN
    }

    private static final class Running {
        final Test test;
        final int logErrorsAtStart;
        int step;
        int waited;

        Running(Test test, int logErrorsAtStart) {
            this.test = test;
            this.logErrorsAtStart = logErrorsAtStart;
        }

        Step currentStep() {
            return test.steps().get(step);
        }
    }
}
