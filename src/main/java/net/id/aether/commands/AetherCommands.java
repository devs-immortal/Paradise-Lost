package net.id.aether.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Consumer;

public class AetherCommands {

    public static void init() {
        register(MoaEggCommand::register);
        register(MoaStatCommand::register);
        register(ConditionCommand::register);
        register(FloatingBlockCommand::register);
    }

    private static void register(Consumer<CommandDispatcher<ServerCommandSource>> command) {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> command.accept(dispatcher)));
    }
}
