package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.client.rendering.entity.state.FloatyMinecartRenderState;
import net.minecraft.client.renderer.entity.state.MinecartRenderState;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(MinecartRenderState.class)
public class MinecartRenderStateMixin implements FloatyMinecartRenderState {
    @Unique
    @Nullable
    private AbstractMinecart paradiseLost$cart;

    @Unique
    private float paradiseLost$partialTick;

    @Override
    @Nullable
    public AbstractMinecart paradiseLost$getCart() {
        return this.paradiseLost$cart;
    }

    @Override
    public float paradiseLost$getPartialTick() {
        return this.paradiseLost$partialTick;
    }

    @Override
    public void paradiseLost$setCart(AbstractMinecart cart, float partialTick) {
        this.paradiseLost$cart = cart;
        this.paradiseLost$partialTick = partialTick;
    }
}
