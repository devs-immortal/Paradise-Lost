package net.id.paradise_lost.clienttest;

import java.util.List;

public record Test(String name, List<Step> steps) {
    public Test(String name, Step... steps) {
        this(name, List.of(steps));
    }
}
