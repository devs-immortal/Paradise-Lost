package net.id.paradise_lost.mixin.client.render;

import net.id.paradise_lost.client.rendering.entity.state.FloatyAnchoredRenderState;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(HumanoidRenderState.class)
public class HumanoidRenderStateMixin implements FloatyAnchoredRenderState {
    @Unique
    private boolean paradiseLost$floatyAnchored;

    @Override
    public boolean paradiseLost$isFloatyAnchored() {
        return this.paradiseLost$floatyAnchored;
    }

    @Override
    public void paradiseLost$setFloatyAnchored(boolean anchored) {
        this.paradiseLost$floatyAnchored = anchored;
    }
}
