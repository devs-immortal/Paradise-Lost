package net.id.paradise_lost.services;

import net.id.paradise_lost.attachments.CommonDataAttachment;
import net.id.paradise_lost.platform.services.IAttachmentHelper;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class NeoForgeAttachmentHelper implements IAttachmentHelper {
    @SuppressWarnings("unchecked")
    @Override
    public <T> void registerDataAttachment(CommonDataAttachment<T> attachment) {
        AttachmentType.Builder<T> builder =
                AttachmentType.builder((Function<IAttachmentHolder, T>) (Object) attachment.getDefaultValueSupplier());
        if (attachment.getCodec() != null) {
            builder.serialize(attachment.getCodec());
        }
        if (attachment.isCopyOnDeath()) {
            builder.copyOnDeath();
        }
        if (attachment.canSync()) {
            builder.sync(attachment.getStreamCodec());
        }
        AttachmentType<T> type = builder.build();
        Registry.register(NeoForgeRegistries.ATTACHMENT_TYPES, attachment.getName(), type);
        attachment.setAttachment(type);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof IAttachmentHolder holder) {
            return holder.getData(type);
        }
        throw new IllegalStateException("Cannot attach data to " + object);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value) {
        AttachmentType<T> type = (AttachmentType<T>) attachment.getAttachment();
        if (object instanceof IAttachmentHolder holder) {
            if (value == null) {
                holder.removeData(type);
            } else {
                holder.setData(type, value);
            }
        } else {
            throw new IllegalStateException("Cannot attach data to " + object);
        }
    }
}
