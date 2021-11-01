package net.id.aether.util;

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
    
    public static final class Music{
        public static final MusicSound AETHER = new MusicSound(MUSIC_AETHER, 12000, 24000, false);
        
        //Triggers <clinit>()V
        private static void init(){}
    }
    
    public static void init(){
        register(BLOCK_AETHER_PORTAL_AMBIENT);
        register(BLOCK_AETHER_PORTAL_TRAVEL);
        register(BLOCK_AETHER_PORTAL_TRIGGER);
        register(MUSIC_AETHER);
        register(MISC_SILENCE);
        
        Music.init();
    }
    
    private static void register(SoundEvent event){
        Registry.register(Registry.SOUND_EVENT, event.getId(), event);
    }
}
