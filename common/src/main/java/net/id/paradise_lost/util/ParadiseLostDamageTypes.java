package net.id.paradise_lost.util;

import net.id.paradise_lost.ParadiseLost;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;
import net.id.paradise_lost.ModConstants;

public class ParadiseLostDamageTypes {

    public static final ResourceKey<DamageType> FALL_FROM_PARADISE = ResourceKey.create(Registries.DAMAGE_TYPE, ModConstants.id("fall"));

    public static DamageSource of(Level world, ResourceKey<DamageType> key) {
        return new DamageSource(world.registryAccess().lookupOrThrow(Registries.DAMAGE_TYPE).getOrThrow(key));
    }

    public static void init() {
    }

}
