package net.id.paradiselost.util;

import net.id.paradiselost.ParadiseLost;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

public class ParadiseLostDamageTypes {

    public static final RegistryKey<DamageType> FALL_FROM_PARADISE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, ParadiseLost.locate("fall"));

    public static DamageSource of(World world, RegistryKey<DamageType> key) {
        return new DamageSource(world.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(key));
    }

    public static void init() {}

}
