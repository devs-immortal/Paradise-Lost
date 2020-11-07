package com.aether.items.accessories;

import net.minecraft.util.collection.Int2ObjectBiMap;

public enum AccessoryTypes {
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

    AccessoryTypes(String displayName, int durability, int damageReduced) {
        this.displayName = displayName;
        this.durability = durability;
        this.damagedReduced = damageReduced;
    }

    public static Int2ObjectBiMap<AccessoryTypes> createCompleteList() {
        Int2ObjectBiMap<AccessoryTypes> map = new Int2ObjectBiMap<AccessoryTypes>(8);

        map.put(PENDANT, 0);
        map.put(CAPE, 1);
        map.put(SHIELD, 2);
        map.put(MISC, 3);
        map.put(RING, 4);
        map.put(EXTRA_RING, 5);
        map.put(GLOVES, 6);
        map.put(EXTRA_MISC, 7);

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