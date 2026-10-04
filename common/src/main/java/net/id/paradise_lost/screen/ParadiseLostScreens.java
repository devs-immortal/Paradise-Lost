package net.id.paradise_lost.screen;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.registration.RegistrationProvider;
import net.id.paradise_lost.registration.RegistryObject;
import net.id.paradise_lost.screen.handler.MoaScreenHandler;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public final class ParadiseLostScreens {
    public static final RegistrationProvider<MenuType<?>> MENUS =
            RegistrationProvider.get(Registries.MENU, ModConstants.MODID);

    public static RegistryObject<MenuType<?>, MenuType<MoaScreenHandler>> MOA;

    private ParadiseLostScreens() {}

    @SuppressWarnings("unchecked")
    public static void registerMoa(Supplier<MenuType<MoaScreenHandler>> factory) {
        MOA = (RegistryObject<MenuType<?>, MenuType<MoaScreenHandler>>) (RegistryObject<?, ?>)
                MENUS.register("moa", factory);
    }

    public static void init() {}
}
