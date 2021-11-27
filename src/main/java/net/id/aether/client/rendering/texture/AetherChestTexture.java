package net.id.aether.client.rendering.texture;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.SpriteIdentifier;

import java.util.Calendar;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import static net.id.aether.Aether.locate;
import static net.minecraft.client.render.TexturedRenderLayers.CHEST_ATLAS_TEXTURE;

@Environment(EnvType.CLIENT)
public enum AetherChestTexture{
    SKYROOT,
//    GOLDEN_OAK,
//    ORANGE,
//    CRYSTAL,
//    WISTERIA,
    ;
    
    public final SpriteIdentifier single;
    public final SpriteIdentifier left;
    public final SpriteIdentifier right;
    
    AetherChestTexture(){
        // return new SpriteIdentifier(CHEST_ATLAS_TEXTURE, new Identifier("entity/chest/" + variant));
        String name = name().toLowerCase(Locale.ROOT);
        var prefix = Holiday.HOLIDAY.getPrefix();
        single = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix));
        left = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix + "_left"));
        right = new SpriteIdentifier(CHEST_ATLAS_TEXTURE, locate("entity/chest/" + name + '/' + prefix + "_right"));
    }
    
    Set<SpriteIdentifier> textures(){
        return Set.of(single, left, right);
    }
    
    public SpriteIdentifier single(){
        return single;
    }
    
    public SpriteIdentifier left(){
        return left;
    }
    
    public SpriteIdentifier right(){
        return right;
    }
    
    public enum Holiday{
        NONE("normal", (calendar)->false),
        CHRISTMAS("christmas", (calendar)->{
            int date = calendar.get(Calendar.DATE);
            return calendar.get(Calendar.MONTH) == Calendar.DECEMBER && date >= 24 && date <= 26;
        }),
        ;
        
        private final String prefix;
        private final Predicate<Calendar> predicate;
        
        Holiday(String prefix, Predicate<Calendar> predicate){
            this.prefix = prefix;
            this.predicate = predicate;
        }
        
        private static final Holiday HOLIDAY = getHoliday();
        
        private static Holiday getHoliday(){
            Calendar calendar = Calendar.getInstance();
            for(var value : values()){
                if(value.predicate.test(calendar)){
                    return value;
                }
            }
            return NONE;
        }
    
        public String getPrefix(){
            return prefix;
        }
    }
}
