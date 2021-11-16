package net.id.aether.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.id.aether.screen.handler.LoreHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;

import static net.id.aether.Aether.locate;

public final class AetherScreens{
    private AetherScreens(){}
    
    public static final ScreenHandlerType<LoreHandler> LORE = register("lore", LoreHandler::new);
    
    public static void init(){}
    
    @Environment(EnvType.CLIENT)
    public static void initClient(){
        register(LORE, LoreScreen::new);
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
}
