package net.id.paradise_lost.client.rendering.entity.state;

// Added to HumanoidRenderState by HumanoidRenderStateMixin, so the armor layer can tell
// anchored floaty leggings apart without access to the entity.
public interface FloatyAnchoredRenderState {
    boolean paradiseLost$isFloatyAnchored();

    void paradiseLost$setFloatyAnchored(boolean anchored);
}
