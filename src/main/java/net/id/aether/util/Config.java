package net.id.aether.util;

import java.util.function.Function;
import net.fabricmc.loader.api.FabricLoader;

import static net.id.aether.Aether.MOD_ID;

public final class Config{
    private Config(){}
    
    //TODO When sodium supports custom render layers, make this check the version
    public static final boolean SODIUM_WORKAROUND = getBoolean("sodium_workaround", isLoaded("sodium"));
    
    private static boolean isLoaded(String id){
        return FabricLoader.getInstance().isModLoaded(id);
    }
    
    private static boolean getBoolean(String key, boolean defaultValue){
        return get(key, Boolean::parseBoolean, defaultValue);
    }
    
    private static <T> T get(String key, Function<String, T> parser, T defaultValue){
        var value = System.getProperty(MOD_ID + '.' + key);
        if(value != null && !value.isBlank()){
            try{
                return parser.apply(value);
            }catch(Throwable t){
                System.err.println("Failed to parse " + MOD_ID + '.' + key + ", using default value");
                t.printStackTrace();
            }
        }
        return defaultValue;
    }
}
