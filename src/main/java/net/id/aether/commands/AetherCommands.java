package net.id.aether.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.id.aether.commands.devel.LUVCommand;
import net.id.aether.devel.AetherDevel;
import net.minecraft.server.command.ServerCommandSource;

import java.util.function.Consumer;

public class AetherCommands {

    public static void init() {
        register(MoaEggCommand::register);
        register(MoaStatCommand::register);
        register(ConditionCommand::register);
        register(FloatingBlockCommand::register);
        register(LoreCommand::register);

        if(AetherDevel.isDevel()) {
            register(LUVCommand::register);
        }
    }

    private static void register(Consumer<CommandDispatcher<ServerCommandSource>> command) {
        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> command.accept(dispatcher)));
    }
}
