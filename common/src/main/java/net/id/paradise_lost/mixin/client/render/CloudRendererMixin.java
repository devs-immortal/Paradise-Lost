package net.id.paradise_lost.mixin.client.render;

public final class CloudRendererMixin {
    // TODO(1.21.2): in 1.21.2 cloud rendering was moved to net.minecraft.client.renderer.CloudRenderer
    // the original implementation of this mixin was cancelling LevelRenderer#renderClouds
    // in the PL dimension and rendered the cloud buffer 3 times with different heights, scales and scroll speeds
    // the new CloudRenderer#render has no scale hook, so we either need to give up some faithfulness or
    // figure out a new way to implement this
}
