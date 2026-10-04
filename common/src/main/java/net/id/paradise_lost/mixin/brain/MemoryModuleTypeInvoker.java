package net.id.paradise_lost.mixin.brain;

import com.mojang.serialization.Codec;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MemoryModuleType.class)
public interface MemoryModuleTypeInvoker {

    @Invoker
    static <U> MemoryModuleType<U> invokeRegister(String id, Codec<U> codec) {
        throw new AssertionError();
    }

}
