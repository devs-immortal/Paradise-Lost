package net.id.paradise_lost.networking;

import net.id.paradise_lost.ModConstants;
import net.id.paradise_lost.attachments.CommonDataAttachment;
import net.id.paradise_lost.attachments.CommonDataAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.network.PacketDistributor;

// NeoForge 21.3 data attachments cannot sync on their own (Fabric's can), so synced attachments are sent with this packet.
public record AttachmentSyncS2CPacket(int entityId, CommonDataAttachment<?> attachment, Object value) implements CustomPacketPayload {
    public static final Type<AttachmentSyncS2CPacket> TYPE = new Type<>(ModConstants.id("attachment_sync"));
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static final StreamCodec<RegistryFriendlyByteBuf, AttachmentSyncS2CPacket> CODEC = StreamCodec.of(
            (buf, packet) -> {
                buf.writeVarInt(packet.entityId());
                buf.writeResourceLocation(packet.attachment().getName());
                ((StreamCodec) packet.attachment().getStreamCodec()).encode(buf, packet.value());
            },
            buf -> {
                int entityId = buf.readVarInt();
                ResourceLocation name = buf.readResourceLocation();
                CommonDataAttachment<?> attachment = CommonDataAttachments.lookup(name);
                if (attachment == null || !attachment.canSync()) {
                    throw new IllegalStateException("Unknown synced attachment " + name);
                }
                return new AttachmentSyncS2CPacket(entityId, attachment, attachment.getStreamCodec().decode(buf));
            }
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    @SuppressWarnings("unchecked")
    public void handleClient() {
        if (Minecraft.getInstance().level == null) {
            return;
        }
        Entity entity = Minecraft.getInstance().level.getEntity(this.entityId);
        if (entity != null) {
            entity.setData((AttachmentType<Object>) this.attachment.getAttachment(), this.value);
        }
    }

    public static void sendToTracking(Entity entity, CommonDataAttachment<?> attachment, Object value) {
        PacketDistributor.sendToPlayersTrackingEntityAndSelf(entity, new AttachmentSyncS2CPacket(entity.getId(), attachment, value));
    }

    @SuppressWarnings("unchecked")
    public static void sendAll(Entity entity, ServerPlayer receiver) {
        for (CommonDataAttachment<?> attachment : CommonDataAttachments.all()) {
            AttachmentType<Object> type = (AttachmentType<Object>) attachment.getAttachment();
            if (attachment.canSync() && entity.hasData(type)) {
                PacketDistributor.sendToPlayer(receiver, new AttachmentSyncS2CPacket(entity.getId(), attachment, entity.getData(type)));
            }
        }
    }
}
