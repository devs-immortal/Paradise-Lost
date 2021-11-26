package net.id.aether.commands.devel;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.id.aether.component.LUV;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class LUVCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("LUVdevel")
                .requires((source)->source.hasPermissionLevel(2))
                .then(literal("query")
                        .then(argument("target", EntityArgumentType.player())
                                .executes((context) -> queryLUV(context.getSource(), EntityArgumentType.getPlayer(context, "target")))
                        )
                )
                .then(literal("set")
                        .then(argument("target", EntityArgumentType.player())
                                .then(argument("value", IntegerArgumentType.integer(-128, 128))
                                        .executes((context) -> setLUV(context.getSource(), EntityArgumentType.getPlayer(context, "target"), (byte) IntegerArgumentType.getInteger(context, "value")))
                                )
                        )
                )
        );
    }

    public static int queryLUV(ServerCommandSource source, PlayerEntity target) {
        source.sendFeedback(new LiteralText("LUV" + LUV.getLUV(target).getValue()), false);
        return 1;
    }

    public static int setLUV(ServerCommandSource source, PlayerEntity target, byte value) {
        LUV.getLUV(target).setValue(value);
        return 1;
    }
}
