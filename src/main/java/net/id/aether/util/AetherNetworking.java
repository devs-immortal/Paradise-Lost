package net.id.aether.util;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import static net.id.aether.Aether.locate;

public final class AetherNetworking{
    private AetherNetworking(){
        throw new RuntimeException();
    }
    
    public static final Identifier PLAY_PORTAL_TRAVEL_SOUND = locate("play_portal_travel_sound");
    
    @Environment(EnvType.CLIENT)
    public static void initClient(){
        ClientPlayNetworking.registerGlobalReceiver(PLAY_PORTAL_TRAVEL_SOUND, (client, handler, buffer, sender)->{
            var random = client.world == null ? new Random() : client.world.random;
            client.getSoundManager().play(PositionedSoundInstance.ambient(AetherSoundEvents.BLOCK_AETHER_PORTAL_TRAVEL, random.nextFloat() * 0.4F + 0.8F, 0.25F));
        });
    }
    
    public static void init(){
    
    }
}
