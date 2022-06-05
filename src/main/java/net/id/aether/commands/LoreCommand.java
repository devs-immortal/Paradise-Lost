package net.id.aether.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.id.aether.component.AetherComponents;
import net.id.aether.lore.LoreStatus;
import net.id.aether.registry.AetherRegistries;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

final class LoreCommand{
    private LoreCommand(){}
    
    private static final SuggestionProvider<ServerCommandSource> LORE_SUGGESTER = (context, builder)->{
        AetherRegistries.LORE_REGISTRY.getIds().forEach((id)->builder.suggest(id.toString()));
        return builder.buildFuture();
    };
    
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher){
        dispatcher.register(literal("aetherLore")
            .requires((source)->source.hasPermissionLevel(2))
            .then(literal("grant")
                .then(argument("id", IdentifierArgumentType.identifier())
                    .suggests(LORE_SUGGESTER)
                    .executes((context)->executeGrant(context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))
                )
            )
            .then(literal("revoke")
                .then(argument("id", IdentifierArgumentType.identifier())
                    .suggests(LORE_SUGGESTER)
                    .executes((context)->executeRevoke(context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))
                )
            )
            .then(literal("get")
                .then(argument("id", IdentifierArgumentType.identifier())
                    .suggests(LORE_SUGGESTER)
                    .executes((context)->executeGet(context.getSource(), IdentifierArgumentType.getIdentifier(context, "id")))
                )
            )
        );
    }
    
    private static int executeGrant(ServerCommandSource source, Identifier id) throws CommandSyntaxException{
        var player = source.getPlayer();
        var state = AetherComponents.LORE_STATE.get(player);
        var status = state.getLoreStatus(id);
        if(status == LoreStatus.UNLOCKED){
            state.setLoreStatus(id, LoreStatus.COMPLETED);
            return 1;
        }else{
            return 0;
        }
    }
    
    private static int executeRevoke(ServerCommandSource source, Identifier id) throws CommandSyntaxException{
        var player = source.getPlayer();
        var state = AetherComponents.LORE_STATE.get(player);
        var status = state.getLoreStatus(id);
        if(status == LoreStatus.COMPLETED){
            state.setLoreStatus(id, LoreStatus.UNLOCKED);
            return 1;
        }else{
            return 0;
        }
    }
    
    private static int executeGet(ServerCommandSource source, Identifier id) throws CommandSyntaxException{
        var player = source.getPlayer();
        var state = AetherComponents.LORE_STATE.get(player);
        var status = state.getLoreStatus(id);
        // make this translatable
        source.sendFeedback(Text.translatable("commands.the_aether.lore.get." + switch(status){
            case LOCKED -> "locked";
            case HIDDEN -> "hidden";
            case FREE -> "free";
            case UNLOCKED -> "unlocked";
            case COMPLETED -> "completed";
        }), false);
        return status.ordinal();
    }
}
