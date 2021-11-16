package net.id.aether.mixin.util;

import java.util.Map;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(NbtCompound.class)
public interface NbtCompoundAccessor{
    @Accessor Map<String, NbtElement> getEntries();
}
