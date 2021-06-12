package com.aether.items.accessories;

import net.minecraft.util.CrudeIncrementalIntIdentityHashBiMap;

public enum AccessoryType {
    RING("ring", 11, 3),
    EXTRA_RING("ring", 11, 3),
    PENDANT("pendant", 16, 7),
    CAPE("cape", 15, 5),
    SHIELD("shield", 13, 0),
    GLOVES("gloves", 10, 0),
    MISC("misc", 10, 0),
    EXTRA_MISC("misc", 10, 0);

    private final int durability, damagedReduced;
    private final String displayName;

    AccessoryType(String displayName, int durability, int damageReduced) {
        this.displayName = displayName;
        this.durability = durability;
        this.damagedReduced = damageReduced;
    }

    public static CrudeIncrementalIntIdentityHashBiMap<AccessoryType> createCompleteList() {
        CrudeIncrementalIntIdentityHashBiMap<AccessoryType> map = new CrudeIncrementalIntIdentityHashBiMap<>(8);

        map.addMapping(PENDANT, 0);
        map.addMapping(CAPE, 1);
        map.addMapping(SHIELD, 2);
        map.addMapping(MISC, 3);
        map.addMapping(RING, 4);
        map.addMapping(EXTRA_RING, 5);
        map.addMapping(GLOVES, 6);
        map.addMapping(EXTRA_MISC, 7);

        return map;
    }

    public int getDurability() {
        return this.durability;
    }

    public int getDamageReduced() {
        return this.damagedReduced;
    }

    public String getDisplayName() {
        return this.displayName;
    }
}