package net.id.paradise_lost.commands;

import com.mojang.brigadier.CommandDispatcher;
import net.id.paradise_lost.api.FloatingBlockHelper;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class FloatingBlockCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {

        dispatcher.register(
                literal("gravitate")
                        .requires((source) -> source.hasPermission(2))
                        .then(argument("pos", BlockPosArgument.blockPos())
                                .executes((context -> floatBlock(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos"), false)))
                                .then(literal("force")
                                        .executes((context) -> floatBlock(context.getSource(), BlockPosArgument.getLoadedBlockPos(context, "pos"), true))))
        );
    }

    private static int floatBlock(CommandSourceStack source, BlockPos pos, boolean force) {
        if (!source.getLevel().getBlockState(pos).isAir() && FloatingBlockHelper.ANY.tryCreate(source.getLevel(), pos, force)) {
            source.sendSuccess(() -> Component.translatable("commands.paradise_lost.gravitate.success"), true);
        } else {
            source.sendFailure(Component.translatable("commands.paradise_lost.gravitate.failure"));
        }
        return 1;
    }
}
