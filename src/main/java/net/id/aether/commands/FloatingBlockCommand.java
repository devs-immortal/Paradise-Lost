package net.id.aether.commands;


import com.mojang.brigadier.CommandDispatcher;
import net.id.aether.api.FloatingBlockHelper;
import net.minecraft.command.argument.BlockPosArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FloatingBlockCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        // make this work for entities too, eventually.
        dispatcher.register(
                literal("gravitate")
                        .requires((source) -> source.hasPermissionLevel(2))
                        .then(argument("pos", BlockPosArgumentType.blockPos())
                                .executes((context -> floatBlock(context.getSource(), BlockPosArgumentType.getLoadedBlockPos(context, "pos"), false)))
                                .then(literal("force")
                                        .executes((context) -> floatBlock(context.getSource(), BlockPosArgumentType.getLoadedBlockPos(context, "pos"), true))))
        );
    }

    private static int floatBlock(ServerCommandSource source, BlockPos pos, boolean force) {
        if (!source.getWorld().getBlockState(pos).isAir() && FloatingBlockHelper.ANY.tryCreate(source.getWorld(), pos, force)) {
            source.sendFeedback(new TranslatableText("commands.the_aether.gravitate.success"), true);
        } else {
            source.sendError(new TranslatableText("commands.the_aether.gravitate.failure"));
        }
        return 1;
    }
}
