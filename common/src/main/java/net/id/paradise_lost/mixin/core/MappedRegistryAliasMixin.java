package net.id.paradise_lost.mixin.core;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.id.paradise_lost.util.ParadiseLostAliasFix;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryAliasMixin {

    private MappedRegistry<?> paradiseLost$registry() {
        return (MappedRegistry<?>) (Object) this;
    }

    @ModifyReturnValue(
            method = "get(Lnet/minecraft/resources/ResourceLocation;)Ljava/lang/Object;",
            at = @At("RETURN")
    )
    private Object paradiseLost$aliasGetByLocation(Object original, ResourceLocation id) {
        if (original != null) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(id);
        if (mapped == null) {
            return original;
        }
        return paradiseLost$registry().get(mapped);
    }

    @ModifyReturnValue(
            method = "getHolder(Lnet/minecraft/resources/ResourceLocation;)Ljava/util/Optional;",
            at = @At("RETURN")
    )
    private Optional<?> paradiseLost$aliasGetHolderByLocation(Optional<?> original, ResourceLocation id) {
        if (original != null && !original.isEmpty()) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(id);
        if (mapped == null) {
            return original;
        }
        return paradiseLost$registry().getHolder(mapped);
    }

    @ModifyReturnValue(
            method = "containsKey(Lnet/minecraft/resources/ResourceLocation;)Z",
            at = @At("RETURN")
    )
    private boolean paradiseLost$aliasContainsByLocation(boolean original, ResourceLocation id) {
        if (original) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(id);
        return mapped != null && paradiseLost$registry().containsKey(mapped);
    }

    @ModifyReturnValue(
            method = "get(Lnet/minecraft/resources/ResourceKey;)Ljava/lang/Object;",
            at = @At("RETURN")
    )
    private Object paradiseLost$aliasGetByKey(Object original, ResourceKey<?> key) {
        if (original != null || key == null) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(key.location());
        if (mapped == null) {
            return original;
        }
        return paradiseLost$registry().get(mapped);
    }

    @ModifyReturnValue(
            method = "getHolder(Lnet/minecraft/resources/ResourceKey;)Ljava/util/Optional;",
            at = @At("RETURN")
    )
    private Optional<?> paradiseLost$aliasGetHolderByKey(Optional<?> original, ResourceKey<?> key) {
        if ((original != null && !original.isEmpty()) || key == null) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(key.location());
        if (mapped == null) {
            return original;
        }
        return paradiseLost$registry().getHolder(mapped);
    }

    @ModifyReturnValue(
            method = "containsKey(Lnet/minecraft/resources/ResourceKey;)Z",
            at = @At("RETURN")
    )
    private boolean paradiseLost$aliasContainsByKey(boolean original, ResourceKey<?> key) {
        if (original || key == null) {
            return original;
        }
        ResourceLocation mapped = ParadiseLostAliasFix.resolve(key.location());
        return mapped != null && paradiseLost$registry().containsKey(mapped);
    }
}
