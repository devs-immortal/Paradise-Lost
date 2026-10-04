package net.id.paradise_lost.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.id.paradise_lost.client.screen.MoaScreen;
import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.screen.handler.MoaScreenHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.entity.Entity;

public final class FabricScreens {
    private FabricScreens() {}

    public static void register() {
        ParadiseLostScreens.registerMoa(() -> new ExtendedScreenHandlerType<>((syncId, inventory, data) -> {
            Entity entity = inventory.player.level().getEntity(data.entityId());
            if (!(entity instanceof MoaEntity moa)) {
                return null;
            }
            return new MoaScreenHandler(syncId, inventory, moa.getInventory(), moa);
        }, MoaScreenHandler.MoaScreenData.PACKET_CODEC));
    }

    public static void registerClient() {
        MenuScreens.register(ParadiseLostScreens.MOA.get(), MoaScreen::new);
    }
}
