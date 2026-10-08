package net.id.paradise_lost.clienttest;

import java.util.List;

public record Test(String group, String name, List<Step> steps) {
    public Test(String group, String name, Step... steps) {
        this(group, name, List.of(steps));
    }

    public String fullName() {
        return group + ": " + name;
    }
}
