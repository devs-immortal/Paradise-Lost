package net.id.paradise_lost.platform.services;

import net.id.paradise_lost.attachments.CommonDataAttachment;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IAttachmentHelper {
    <T> void registerDataAttachment(CommonDataAttachment<T> attachment);

    @Nullable
    <T> T getAttachedValue(Object object, CommonDataAttachment<T> attachment);

    <T> void setAttachedValue(Object object, CommonDataAttachment<T> attachment, @Nullable T value);

    default <T> T getOrCreateAttachedValue(Entity entity, CommonDataAttachment<T> attachment) {
        T value = getAttachedValue(entity, attachment);
        if (value != null) {
            return value;
        }
        value = attachment.getDefaultValueSupplier().apply(entity);
        setAttachedValue(entity, attachment, value);
        return value;
    }
}
