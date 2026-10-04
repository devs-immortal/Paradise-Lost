package net.id.paradise_lost.mixin.brain;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;

@Mixin(SensorType.class)
public interface SensorTypeInvoker {

    @Invoker
    static <U extends Sensor<?>> SensorType<U> invokeRegister(String id, Supplier<U> factory) {
        throw new AssertionError();
    }
}
