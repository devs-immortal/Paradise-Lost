package com.aether.audio;

import com.aether.Aether;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.BiomeEffectSoundPlayer;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;

import java.util.Timer;

public class AetherSounds {
    // TODO: Implement SoundEvents

    // Music Discs
    // public static final SoundEvent aether_tune = new SoundEvent(Aether.locate("aether_tune"));

    // Ambience
    public static final SoundEvent above = new SoundEvent(Aether.locate("above"));

    public static Boolean SoundPlaying = false;
    private int IterationsSinceLastPlay = 0;

    public static void initializeClient() {
        Registry.register(Registry.SOUND_EVENT, Aether.locate("above"), AetherSounds.above);
    }
    public void Sched() {
        if(MinecraftClient.getInstance().player == null) return;
        IterationsSinceLastPlay++;
        if(IterationsSinceLastPlay >= 15) AetherSounds.SoundPlaying = false;
        if(MinecraftClient.getInstance().player.world.getRegistryKey().getValue().getPath().equals("the_aether")) {
            if(!AetherSounds.SoundPlaying) return;
            IterationsSinceLastPlay = 0;
            BiomeEffectSoundPlayer.MusicLoop loop = new BiomeEffectSoundPlayer.MusicLoop(AetherSounds.above);
            MinecraftClient.getInstance().getSoundManager().play(loop);
            AetherSounds.SoundPlaying = true;
        }
    }
}
