package net.id.paradiselost.mixin.client.render;

public final class CloudRendererMixin {
    // TODO(1.21.2): in 1.21.2 cloud rendering was moved to net.minecraft.client.render.CloudRenderer
    // the original implementation of this mixin was cancelling WorldRenderer#renderClouds 
    // in the PL dimension an rendered cloud buffer 3 times with different heights, scales and scroll speeds
    // the new renderClouds has no scale hook, so we either need to give up some faithfulness or
    // figure out a new way to implement this
}
