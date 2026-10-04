package net.id.paradise_lost.platform.services;

import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.minecraft.server.level.ServerPlayer;

public interface IMiscHelper {

    void openMoaScreen(ServerPlayer player, MoaEntity moa);
}
