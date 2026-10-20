package net.id.paradise_lost.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletionException;

public final class TestRun {
    private final Logger log;
    private final Hooks hooks;
    private final Deque<Test> tests = new ArrayDeque<>();
    private final List<String> failures = new ArrayList<>();
    private final int testCount;

    private Running running;
    private int testIndex;
    private int wait;
    private boolean finished;

    public TestRun(String loggerName, List<Class<?>> testClasses, int startDelay, Hooks hooks) {
        this.log = LoggerFactory.getLogger(loggerName);
        this.hooks = hooks;
        testClasses.forEach(type -> tests.addAll(TestLoader.load(type)));
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
        wait = startDelay;
    }

    public void tick() {
        if (finished) return;
        if (wait > 0) {
            wait--;
        } else if (running == null) {
            startNextTest();
        } else {
            runStep();
        }
    }

    private void startNextTest() {
        if (!hooks.ready()) return;
        Test test = tests.poll();
        if (test == null) {
            finish();
            return;
        }
        running = new Running(test, Recorder.logErrors.size());
        int settle;
        try {
            settle = hooks.beforeTest(test, testIndex++, testCount);
        } catch (Throwable t) {
            fail(t);
            return;
        }
        wait = settle + running.currentStep().delay();
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
        log.info("PASS {}", running.test.fullName());
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
            log.error("FAIL {}: {}", name, cause.getMessage());
        } else {
            log.error("FAIL {}: {}", name, cause.getMessage(), cause);
        }
        failures.add(name + ": " + cause.getMessage());
        running = null;
    }

    private void finish() {
        finished = true;
        if (!Recorder.logWarnings.isEmpty()) {
            log.warn("{} warnings logged during the run:", Recorder.logWarnings.size());
            Recorder.logWarnings.forEach(w -> log.warn("  {}", w));
        }
        if (failures.isEmpty()) {
            log.info("All tests passed");
        } else {
            log.error("{} tests failed:", failures.size());
            failures.forEach(f -> log.error("  {}", f));
        }
        hooks.finished();
    }

    public interface Hooks {
        default boolean ready() {
            return true;
        }

        int beforeTest(Test test, int index, int count);

        void finished();
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
