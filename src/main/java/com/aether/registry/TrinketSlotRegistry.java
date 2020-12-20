package com.aether.registry;

import dev.emi.trinkets.api.TrinketSlots;

import static com.aether.Aether.locate;

public class TrinketSlotRegistry {
	public static void init() {
		TrinketSlots.addSlot("chest","parachute",locate("textures/item/accessories/parachute_slot.png"));
	}
}
