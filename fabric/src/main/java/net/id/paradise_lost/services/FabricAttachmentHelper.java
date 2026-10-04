package net.id.paradise_lost.services;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.id.paradise_lost.attachments.CommonDataAttachment;
import net.id.paradise_lost.platform.services.IAttachmentHelper;
import org.jetbrains.annotations.Nullable;

public class FabricAttachmentHelper implements IAttachmentHelper {
    @SuppressWarnings("UnstableApiUsage")
    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {
        AttachmentRegistry.Builder<T> builder = AttachmentRegistry.builder();
        if (attachment.isCopyOnDeath()) {
            builder.copyOnDeath();
        }
        builder.initializer(() -> attachment.getDefaultValueSupplier().apply(null));
        if (attachment.getCodec() != null) {
            builder.persistent(attachment.getCodec());
        }
        if (attachment.canSync()) {

            builder.syncWith(attachment.getStreamCodec(), (target, player) -> true);
        }
        attachment.setAttachment(builder.buildAndRegister(attachment.getName()));
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    @Nullable
    @Override
    public <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof AttachmentTarget target) {
            return target.getAttached(type);
        }
        throw new IllegalStateException("Cannot attach data to " + object);
    }

    @SuppressWarnings({"UnstableApiUsage", "unchecked"})
    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof AttachmentTarget target) {
            target.setAttached(type, value);
        } else {
            throw new IllegalStateException("Cannot attach data to " + object);
        }
    }
}
