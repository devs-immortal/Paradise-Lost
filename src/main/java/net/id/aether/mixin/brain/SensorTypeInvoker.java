package net.id.aether.mixin.brain;

import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Supplier;

@Mixin(SensorType.class)
public interface SensorTypeInvoker {

    @Invoker
    static <U extends Sensor<?>> SensorType<U> invokeRegister(String id, Supplier<U> factory) {
        throw new AssertionError();
    }
}
