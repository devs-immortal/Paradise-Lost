package net.id.aether.devel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.id.aether.util.UncheckedWriter;
import net.minecraft.util.Identifier;

public final class AetherDevel{
    private AetherDevel(){}
    
    public static void init(){
        System.out.print("\n".repeat(5) + "The Aether Reborn is in debug mode!" + "\n".repeat(6));
        Runtime.getRuntime().addShutdownHook(new Thread(AetherDevel::save));
    }
    
    private static final boolean isDevel = FabricLoader.getInstance().isDevelopmentEnvironment();
    
    // Just to make things that should be removed at some point, this isn't going anywhere
    @Deprecated(forRemoval = true)
    public static boolean isDevel(){
        return isDevel;
    }
    
    private static void save(){
        var logFile = Path.of("./aether_todo_server.txt");
        
        try(var writer = new UncheckedWriter(Files.newBufferedWriter(logFile, StandardCharsets.UTF_8))){
            dumpStrings(writer, "Bad features", BAD_FEATURES);
        }catch(UncheckedIOException | IOException e){
            System.err.println("Failed to write Aether devel log");
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void dumpIds(UncheckedWriter writer, String message, Collection<Identifier> ids){
        synchronized(ids){
            if(!ids.isEmpty()){
                writer.write(message + ":\n");
                ids.stream()
                    .sorted(Identifier::compareTo)
                    .forEachOrdered((id)->writer.write("    " + id + '\n'));
            }
        }
    }
    
    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    private static void dumpStrings(UncheckedWriter writer, String message, Collection<String> strings){
        synchronized(strings){
            if(!strings.isEmpty()){
                writer.write(message + ":\n");
                strings.stream()
                    .sorted(String::compareTo)
                    .forEachOrdered((id)->writer.write("    " + id + '\n'));
            }
        }
    }
    
    private static final Set<String> BAD_FEATURES = new HashSet<>();
    
    public static void logBadFeature(String feature){
        synchronized(BAD_FEATURES){
            BAD_FEATURES.add(feature);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public static final class Client{
        private static final Set<Identifier> MISSING_TEXTURES = new HashSet<>();
        private static final Set<Identifier> BAD_TEXTURES = new HashSet<>();
        private static final Set<String> MISSING_LANGUAGE_KEYS = new HashSet<>();
        
        private Client(){}
        
        public static void init(){
            Runtime.getRuntime().addShutdownHook(new Thread(Client::save));
        }
        
        private static void save(){
            var logFile = Path.of("./aether_todo_client.txt");
            
            try(var writer = new UncheckedWriter(Files.newBufferedWriter(logFile, StandardCharsets.UTF_8))){
                dumpIds(writer, "Missing textures", MISSING_TEXTURES);
                dumpIds(writer, "Textures with broken metadata", BAD_TEXTURES);
                dumpStrings(writer, "Missing language keys", MISSING_LANGUAGE_KEYS);
            }catch(UncheckedIOException | IOException e){
                System.err.println("Failed to write Aether devel log");
                e.printStackTrace();
            }
        }
    
        public static void logMissingTexture(Identifier identifier){
            synchronized(MISSING_TEXTURES){
                MISSING_TEXTURES.add(identifier);
            }
        }
    
        public static void logBadTexture(Identifier identifier){
            synchronized(BAD_TEXTURES){
                BAD_TEXTURES.add(identifier);
            }
        }
    
        public static void logMissingLanguageKey(String key){
            synchronized(MISSING_LANGUAGE_KEYS){
                MISSING_LANGUAGE_KEYS.add(key);
            }
        }
    }
}
