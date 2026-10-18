package net.id.paradise_lost.client.rendering.entity.state;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.jetbrains.annotations.Nullable;

// Added to MinecartRenderState by MinecartRenderStateMixin. The float pose smoothing keeps its state on the
// cart, so the renderer needs the cart itself, not just a snapshot.
public interface FloatyMinecartRenderState {
    @Nullable
    AbstractMinecart paradiseLost$getCart();

    float paradiseLost$getPartialTick();

    void paradiseLost$setCart(AbstractMinecart cart, float partialTick);
}
