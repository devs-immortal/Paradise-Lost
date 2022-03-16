package net.id.aether.entities.block;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class SliderEntity extends BlockLikeEntity {
    public SliderEntity(EntityType<? extends BlockLikeEntity> entityType, World world) {
        super(entityType, world);
    }

    public SliderEntity(World world, double x, double y, double z) {
        super(AetherEntityTypes.SLIDER, world, x, y, z, AetherBlocks.CRACKED_CARVED_STONE.getDefaultState());
    }

    // Move in a circle. This is obviously not the final behavior.
    @Override
    public void postTickMovement() {
        this.updateVelocity(0.01F, new Vec3d(1D, 0, 0));
        this.setYaw((this.getYaw() + 6F) % 360.0F);
    }
}
