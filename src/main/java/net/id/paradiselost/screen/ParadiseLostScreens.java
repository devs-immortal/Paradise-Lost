package net.id.paradiselost.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.id.paradiselost.entities.passive.moa.MoaEntity;
import net.id.paradiselost.screen.handler.MoaScreenHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandler;

import static net.id.paradiselost.ParadiseLost.locate;

public final class ParadiseLostScreens {
    private ParadiseLostScreens() {
    }

    public static final ExtendedScreenHandlerType<MoaScreenHandler, MoaScreenHandler.MoaScreenData> MOA = register("moa", (syncId, inventory, entityId) -> {
        Entity entity = inventory.player.getWorld().getEntityById(entityId.entityId());
        if (!(entity instanceof MoaEntity moa)) {
            return null;
        }

        return new MoaScreenHandler(syncId, inventory, moa.getInventory(), moa);
    }, MoaScreenHandler.MoaScreenData.PACKET_CODEC);
    
    public static void init() {
    }
    
    @Environment(EnvType.CLIENT)
    public static void initClient() {
        //register(MOA, MoaScreen::new);
    }
//    @Environment(EnvType.CLIENT)
//    private static <T extends ScreenHandler, S extends HandledScreen<T>> void register(ScreenHandlerType<T> type, ScreenRegistry.Factory<T, S> factory) {
//        ScreenRegistry.register(type, factory);
//    }

    private static <T extends ScreenHandler, D extends CustomPayload> ExtendedScreenHandlerType<T, D> register(String name, ExtendedScreenHandlerType.ExtendedFactory<T, D> factory, PacketCodec<RegistryByteBuf, D> codec) {
        return Registry.register(Registries.SCREEN_HANDLER, locate(name), new ExtendedScreenHandlerType<>(factory, codec));
    }
}
