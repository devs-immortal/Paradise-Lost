package net.id.paradise_lost.networking.packet;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.world.ParadiseLostGameRules;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record AnchoredRunMidairS2CPacket(boolean enabled) implements S2CModPacket<RegistryFriendlyByteBuf> {
    public static final Type<AnchoredRunMidairS2CPacket> TYPE = new Type<>(ModConstants.locate("anchored_run_midair"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AnchoredRunMidairS2CPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL, AnchoredRunMidairS2CPacket::enabled,
            AnchoredRunMidairS2CPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @Override
    public void handleClient() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.execute(() -> {
            if (minecraft.level != null) {
                minecraft.level.getGameRules().getRule(ParadiseLostGameRules.RUN_MIDAIR).set(enabled, null);
            }
        });
    }
}
