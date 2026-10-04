package net.id.paradise_lost.screen;

import net.id.paradise_lost.entity.passive.moa.MoaEntity;
import net.id.paradise_lost.screen.handler.MoaScreenHandler;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;

public final class NeoForgeScreens {
    private NeoForgeScreens() {}

    public static void register() {
        ParadiseLostScreens.registerMoa(() -> IMenuTypeExtension.create((windowId, inv, data) -> {
            int entityId = data.readVarInt();
            Entity entity = inv.player.level().getEntity(entityId);
            if (!(entity instanceof MoaEntity moa)) {
                return null;
            }
            return new MoaScreenHandler(windowId, inv, moa.getInventory(), moa);
        }));
    }
}
