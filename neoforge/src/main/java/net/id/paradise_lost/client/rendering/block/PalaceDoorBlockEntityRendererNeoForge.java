package net.id.paradise_lost.client.rendering.block;

import net.id.paradise_lost.block.blockentity.PalaceDoorBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;

public class PalaceDoorBlockEntityRendererNeoForge extends PalaceDoorBlockEntityRenderer {

    private static final double REACH = 8.0;

    public PalaceDoorBlockEntityRendererNeoForge(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public AABB getRenderBoundingBox(PalaceDoorBlockEntity entity) {
        var pos = entity.getBlockPos();

        return new AABB(
                pos.getX() + 0.5 - REACH,
                pos.getY() + 0.5 - REACH,
                pos.getZ() + 0.5 - REACH,
                pos.getX() + 0.5 + REACH,
                pos.getY() + 0.5 + REACH,
                pos.getZ() + 0.5 + REACH
        );
    }
}
