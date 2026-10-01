package net.id.paradise_lost.world;

import net.minecraft.sounds.SoundEvent;

public interface ExplosionExtensions {

    float getPower();

    void affectWorld(boolean particles, SoundEvent customSound);
}
