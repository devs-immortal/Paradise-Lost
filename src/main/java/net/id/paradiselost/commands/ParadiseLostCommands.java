package net.id.paradiselost.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Consumer;

public class ParadiseLostCommands {

    public static void init() {
        register(MoaEggCommand::register);
        register(MoaStatCommand::register);
        register(FloatingBlockCommand::register);
    }

    private static void register(Consumer<CommandDispatcher<ServerCommandSource>> command) {
        CommandRegistrationCallback.EVENT.register(((dispatcher, registryAccess, environment) -> command.accept(dispatcher)));
    }
}
