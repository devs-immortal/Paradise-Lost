package net.id.paradise_lost.networking.packet;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.entity.ParadiseLostEntityExtensions;
import net.id.paradise_lost.item.armor.FloatyLeggingsItem;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

public record FloatyAnchorC2SPacket(boolean anchored) implements C2SModPacket<RegistryFriendlyByteBuf> {
    public static final Type<FloatyAnchorC2SPacket> TYPE = new Type<>(ModConstants.id("floaty_anchor"));
    public static final StreamCodec<RegistryFriendlyByteBuf, FloatyAnchorC2SPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, FloatyAnchorC2SPacket::anchored,
            FloatyAnchorC2SPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleServer(ServerPlayer player) {
        if (!(player instanceof ParadiseLostEntityExtensions extensions)) {
            return;
        }
        if (anchored && !FloatyLeggingsItem.canAnchor(player)) {
            extensions.setFloatyAnchored(false);
            return;
        }
        if (!anchored || FloatyLeggingsItem.canAnchor(player)) {
            extensions.setFloatyAnchored(anchored);
            if (anchored) {
                player.resetFallDistance();
            }
        }
    }
}
