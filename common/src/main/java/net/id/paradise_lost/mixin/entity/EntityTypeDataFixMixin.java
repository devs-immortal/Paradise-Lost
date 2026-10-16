package net.id.paradise_lost.mixin.entity;

import net.id.paradise_lost.datafixer.ParadiseLostEntityDataFixers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityType.class)
public class EntityTypeDataFixMixin {

    @ModifyVariable(
            method = "create(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;)Ljava/util/Optional;",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private static CompoundTag paradiseLost$fixCreate(CompoundTag tag) {
        ParadiseLostEntityDataFixers.fixEntityTag(tag);
        return tag;
    }

    @ModifyVariable(
            method = "loadEntityRecursive(Lnet/minecraft/nbt/CompoundTag;Lnet/minecraft/world/level/Level;Ljava/util/function/Function;)Lnet/minecraft/world/entity/Entity;",
            at = @At("HEAD"),
            argsOnly = true,
            ordinal = 0
    )
    private static CompoundTag paradiseLost$fixLoadRecursive(CompoundTag tag) {
        ParadiseLostEntityDataFixers.fixEntityTag(tag);
        return tag;
    }
}
