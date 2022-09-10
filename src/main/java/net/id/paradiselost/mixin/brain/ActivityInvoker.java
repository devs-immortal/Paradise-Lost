package net.id.paradiselost.mixin.brain;

import net.minecraft.entity.ai.brain.Activity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Activity.class)
public interface ActivityInvoker {

    @Invoker
    static Activity invokeRegister(String id) {
        throw new AssertionError();
    }
}
