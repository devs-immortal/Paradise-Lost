package net.id.paradiselost.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.screen.handler.LoreHandler;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.id.paradiselost.screen.slot.PreviewSlot;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.Entity;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import static net.id.paradiselost.ParadiseLost.locate;
import static net.minecraft.screen.PlayerScreenHandler.BLOCK_ATLAS_TEXTURE;

public final class ParadiseLostScreens {
    private ParadiseLostScreens(){}
    
    public static final ScreenHandlerType<LoreHandler> LORE = register("lore", LoreHandler::new);
    public static final ScreenHandlerType<MoaScreenHandler> MOA = register("moa", (syncId, inventory, buffer)->{
        Entity entity = inventory.player.world.getEntityById(buffer.readVarInt());
        if(!(entity instanceof MoaEntity moa)){
            return null;
        }
        
        return new MoaScreenHandler(syncId, inventory, moa.getInventory(), moa);
    });
    
    public static void init(){}
    
    @Environment(EnvType.CLIENT)
    public static void initClient(){
        register(LORE, LoreScreen::new);
        register(MOA, MoaScreen::new);
    }
    
    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerRegistry.SimpleClientHandlerFactory<T> factory){
        return ScreenHandlerRegistry.registerSimple(locate(name), factory);
    }
    
    private static <T extends ScreenHandler> ScreenHandlerType<T> register(String name, ScreenHandlerRegistry.ExtendedClientHandlerFactory<T> factory){
        return ScreenHandlerRegistry.registerExtended(locate(name), factory);
    }
    
    @Environment(EnvType.CLIENT)
    private static <T extends ScreenHandler, S extends HandledScreen<T>> void register(ScreenHandlerType<T> type, ScreenRegistry.Factory<T, S> factory){
        ScreenRegistry.register(type, factory);
    }
    
    @Environment(EnvType.CLIENT)
    public static void clientInit() {
        // Registers the custom slot textures
        ClientSpriteRegistryCallback.event(BLOCK_ATLAS_TEXTURE).register((atlas, registry)->{
            for (PreviewSlot.Image value : PreviewSlot.Image.values()) {
                registry.register(value.location());
            }
        });
    }
}
