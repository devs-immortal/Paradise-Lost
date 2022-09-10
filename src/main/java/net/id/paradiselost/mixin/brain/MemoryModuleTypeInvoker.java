package net.id.paradiselost.mixin.brain;

import com.mojang.serialization.Codec;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;



@Mixin(MemoryModuleType.class)
public interface MemoryModuleTypeInvoker {

    @Invoker
    static <U> MemoryModuleType<U> invokeRegister(String id, Codec<U> codec) {
        throw new AssertionError();
    }

   // @Invoker("register")
   // static <U> MemoryModuleType<U> invokeRegister(String id) {
   //     throw new AssertionError();
   // }
}
