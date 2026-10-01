package net.id.paradise_lost.services;

import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.platform.services.IMiscHelper;
import net.id.paradise_lost.screen.handler.MoaScreenHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NeoForgeMiscHelper implements IMiscHelper {
    @Override
    public void openMoaScreen(ServerPlayer player, MoaEntity moa) {
        player.openMenu(new MenuProvider() {
            @Override
            public Component getDisplayName() {
                return Component.translatable("container.paradise_lost.moa");
            }

            @Override
            public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
                return new MoaScreenHandler(syncId, inv, moa.getInventory(), moa);
            }
        }, buf -> buf.writeVarInt(moa.getId()));
    }
}
