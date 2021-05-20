package com.aether.items.tools;

import dev.emi.trinkets.api.TrinketItem;

public class Parachute extends TrinketItem {

    public Parachute(Settings settings) {
        super(settings);
    }

    @Override
    public boolean canWearInSlot(String s, String s1) {
        return s.equals("chest") && s1.equals("parachute");
    }
}