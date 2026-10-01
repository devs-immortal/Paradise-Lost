package net.id.paradise_lost.component;

import net.minecraft.world.entity.vehicle.AbstractMinecart;

public final class MinecartFloating {
    private MinecartFloating() {}

    public static FloatingComponent get(AbstractMinecart cart) {
        return new FloatingComponent(cart);
    }

    public static void sync(AbstractMinecart cart) {}
}
