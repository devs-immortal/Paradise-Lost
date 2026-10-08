package net.id.paradise_lost.clienttest;

import java.util.function.BooleanSupplier;

public sealed interface Step {
    int delay();

    record Run(int delay, Runnable action) implements Step {
    }

    record Until(String what, int timeout, BooleanSupplier condition) implements Step {
        @Override
        public int delay() {
            return 0;
        }
    }

    static Step run(int delay, Runnable action) {
        return new Run(delay, action);
    }

    static Step until(String what, int timeout, BooleanSupplier condition) {
        return new Until(what, timeout, condition);
    }
}
