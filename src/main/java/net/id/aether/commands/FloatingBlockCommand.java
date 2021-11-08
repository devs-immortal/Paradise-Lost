package net.id.aether.commands;


import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.id.aether.entities.util.floatingblock.FloatingBlockHelper;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class FloatingBlockCommand {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
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
        if (!source.getWorld().getBlockState(pos).isAir() && FloatingBlockHelper.tryCreate(source.getWorld(), pos, force)) {
            source.sendFeedback(new LiteralText("Successfully floated the block(s)."), true);
        } else {
            source.sendError(new LiteralText("Couldn't float the block(s)."));
        }
        return 1;
    }
}
