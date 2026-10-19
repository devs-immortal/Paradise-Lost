package net.id.paradise_lost.networking.packet;

import net.id.paradise_lost.ModConstants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public record FoodBowlCtrlC2SPacket(boolean dumpStack) implements C2SModPacket<RegistryFriendlyByteBuf> {
    public static final Type<FoodBowlCtrlC2SPacket> TYPE = new Type<>(ModConstants.locate("food_bowl_ctrl"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodBowlCtrlC2SPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, FoodBowlCtrlC2SPacket::dumpStack,
            FoodBowlCtrlC2SPacket::new
    );

    private static final Map<UUID, Pending> PENDING = new ConcurrentHashMap<>();

    private record Pending(boolean dumpStack, long gameTime) {
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        PENDING.put(player.getUUID(), new Pending(dumpStack, player.level().getGameTime()));
    }

    public static boolean pollDump(ServerPlayer player) {
        Pending pending = PENDING.remove(player.getUUID());
        if (pending == null || !pending.dumpStack) {
            return false;
        }
        return pending.gameTime == player.level().getGameTime();
    }
}
