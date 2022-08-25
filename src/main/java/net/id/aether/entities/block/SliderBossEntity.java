package net.id.aether.entities.block;

import net.id.aether.entities.AetherEntityTypes;
import net.id.incubus_core.blocklikeentities.api.BlockLikeEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;

public class SliderBossEntity extends SliderEntity {
    private static final int MAX_WINDED_TICKS = 40;
    private static final int MAX_PRIMING_TICKS = 20;
    private static final int PLAYER_DETECTION_RADIUS = 25;

    public SliderBossEntity(EntityType<? extends SliderBossEntity> entityType, World world) {
        super(entityType, world);
    }

    public SliderBossEntity(World world, BlockPos pos) {
        super(AetherEntityTypes.SLIDER_BOSS, world, pos);
        calculateBoundingBox();
    }

    @Override
    protected Box calculateBoundingBox() {
        return VoxelShapes.fullCube().getBoundingBox()
                .expand(2)
                .offset(getPos().subtract(new Vec3d(0.5, -2, 0.5)));
    }


    @Override
    public void postTickMovement() {
        switch (getState()) {
            case SLIDING -> tickSlide(0.005F);
            case WINDED -> tickWinded(MAX_WINDED_TICKS);
            case DORMANT -> tickDormant(PLAYER_DETECTION_RADIUS, true);
            case PRIMING -> tickPriming(MAX_PRIMING_TICKS);
        }
    }
}
