package net.id.paradise_lost.test.mixin;

import net.id.paradise_lost.test.TickEvents;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;

@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin {
    @Inject(method = "tickServer", at = @At("TAIL"))
    private void testmod$endTick(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        MinecraftServer server = (MinecraftServer) (Object) this;
        TickEvents.SERVER_END.forEach(listener -> listener.accept(server));
    }
}
