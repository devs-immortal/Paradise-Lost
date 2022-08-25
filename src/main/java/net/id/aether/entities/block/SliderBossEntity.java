package net.id.aether.entities.block;

import net.id.aether.entities.AetherEntityTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class SliderBossEntity extends SliderEntity {
    private static final int MAX_WINDED_TICKS = 40;
    private static final int MAX_PRIMING_TICKS = 20;

    public SliderBossEntity(EntityType<? extends SliderBossEntity> entityType, World world) {
        super(entityType, world);
    }

    public SliderBossEntity(World world, BlockPos pos) {
        super(AetherEntityTypes.SLIDER_BOSS, world, pos.up().up());
        this.calculateBoundingBox();
    }

    @Override
    protected Box calculateBoundingBox() {
        this.collides = true;
        return VoxelShapes.fullCube().getBoundingBox()
                .expand(2)
                .offset(getPos().subtract(new Vec3d(0.5, -2, 0.5)));
    }

    @Override
    public void postTickMovement() {
        switch (getState()) {
            case SLIDING -> tickSlide();
            case WINDED -> tickWinded(MAX_WINDED_TICKS);
            case DORMANT -> tickDormant();
            case PRIMING -> tickPriming(MAX_PRIMING_TICKS);
        }
    }

    @Override
    protected float getMoveSpeed() {
        return !this.getSteppingBlockState().isAir() ? 0.005F : 0.02F;
    }

    @Override
    protected int getPlayerDetectionRadius() {
        return 25;
    }

    @Override
    protected Direction getNextDirection(Entity entity) {
        Vec3d displacement = entity.getPos().subtract(this.getPos());
        if (displacement.lengthSquared() > 14 * 14 && displacement.y > -1) {
            return Direction.UP;
        }
        return Direction.getFacing(displacement.x, displacement.y, displacement.z);
    }
}
