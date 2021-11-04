package net.id.aether.util;

import java.lang.reflect.Modifier;
import net.minecraft.sound.MusicSound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import static net.id.aether.Aether.locate;

//TODO Is this the best place for this?
public final class AetherSoundEvents{
    private AetherSoundEvents(){
        throw new RuntimeException();
    }
    
    public static final SoundEvent BLOCK_AETHER_PORTAL_AMBIENT = new SoundEvent(locate("block.portal.ambient"));
    public static final SoundEvent BLOCK_AETHER_PORTAL_TRAVEL = new SoundEvent(locate("block.portal.travel"));
    public static final SoundEvent BLOCK_AETHER_PORTAL_TRIGGER = new SoundEvent(locate("block.portal.trigger"));
    
    public static final SoundEvent MUSIC_AETHER = new SoundEvent(locate("music.aether"));
    
    public static final SoundEvent MISC_SILENCE = new SoundEvent(locate("misc.silence"));
    
    public static final SoundEvent ENTITY_COCKATRICE_AMBIENT = new SoundEvent(locate("entity.cockatrice.ambient"));
    public static final SoundEvent ENTITY_COCKATRICE_DEATH = new SoundEvent(locate("entity.cockatrice.death"));
    public static final SoundEvent ENTITY_COCKATRICE_HURT = new SoundEvent(locate("entity.cockatrice.hurt"));
    public static final SoundEvent ENTITY_MOA_AMBIENT = new SoundEvent(locate("entity.moa.ambient"));
    public static final SoundEvent ENTITY_MOA_DEATH = new SoundEvent(locate("entity.moa.death"));
    public static final SoundEvent ENTITY_MOA_HURT = new SoundEvent(locate("entity.moa.hurt"));
    
    public static final class Music{
        public static final MusicSound AETHER = new MusicSound(MUSIC_AETHER, 12000, 24000, false);
        
        //Triggers <clinit>()V
        private static void init(){}
    }
    
    public static void init(){
        /*
        register(BLOCK_AETHER_PORTAL_AMBIENT);
        register(BLOCK_AETHER_PORTAL_TRAVEL);
        register(BLOCK_AETHER_PORTAL_TRIGGER);
        register(MUSIC_AETHER);
        register(MISC_SILENCE);
         */
        // I'm lazy
        int modifier = Modifier.PUBLIC | Modifier.STATIC | Modifier.FINAL;
        for(var field : AetherSoundEvents.class.getDeclaredFields()){
            if(field.getType() == SoundEvent.class && field.getModifiers() == modifier){
                field.setAccessible(true);
                try{
                    register((SoundEvent)field.get(null));
                }catch(IllegalAccessException e){
                    e.printStackTrace();
                }
            }
        }
        
        Music.init();
    }
    
    private static void register(SoundEvent event){
        Registry.register(Registry.SOUND_EVENT, event.getId(), event);
    }
}
