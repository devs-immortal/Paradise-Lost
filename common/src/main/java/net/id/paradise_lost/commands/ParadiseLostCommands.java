package net.id.paradise_lost.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public final class ParadiseLostCommands {
    private static final List<Consumer<CommandDispatcher<CommandSourceStack>>> COMMANDS = new ArrayList<>();

    private ParadiseLostCommands() {}

    public static void init() {
        register(MoaEggCommand::register);
        register(MoaStatCommand::register);
        register(FloatingBlockCommand::register);
    }

    public static void registerAll(CommandDispatcher<CommandSourceStack> dispatcher) {
        COMMANDS.forEach(command -> command.accept(dispatcher));
    }

    static List<Consumer<CommandDispatcher<CommandSourceStack>>> commands() {
        return Collections.unmodifiableList(COMMANDS);
    }

    private static void register(Consumer<CommandDispatcher<CommandSourceStack>> command) {
        COMMANDS.add(command);
    }
}
