package net.id.paradiselost.mixin.earlyrisers;

import com.chocohead.mm.api.ClassTinkerers;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.MappingResolver;

public class CustomBoatEarlyRiser implements Runnable {

    @Override
    public void run() {
        MappingResolver remapper  = FabricLoader.getInstance().getMappingResolver();

        String boatType = remapper.mapClassName("intermediary", "net.minecraft.class_1690$class_1692");
        String block = 'L' + remapper.mapClassName("intermediary", "net.minecraft.class_2248") + ';';
        ClassTinkerers.enumBuilder(boatType, block, "Ljava/lang/String;")
                .addEnum("PARADISE_LOST_AUREL", () -> new Object[] {null, "paradise_lost_aurel"})
                .addEnum("PARADISE_LOST_MOTHER_AUREL", () -> new Object[] {null, "paradise_lost_mother_aurel"})
                .addEnum("PARADISE_LOST_ORANGE", () -> new Object[] {null, "paradise_lost_orange"})
                .addEnum("PARADISE_LOST_WISTERIA", () -> new Object[] {null, "paradise_lost_wisteria"})
                .build();
    }

}
