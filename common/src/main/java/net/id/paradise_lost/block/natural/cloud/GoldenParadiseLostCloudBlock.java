package net.id.paradise_lost.block.natural.cloud;

import net.id.paradise_lost.registry.BlockRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GoldenParadiseLostCloudBlock extends ParadiseLostCloudBlock {

    protected static VoxelShape SHAPE = Shapes.empty();

    public GoldenParadiseLostCloudBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level world, BlockPos pos, Entity entity) {
        entity.fallDistance = 0.0F;
        Vec3 motion = entity.getDeltaMovement();
        BlockPos position = entity.blockPosition();

        if ((Math.abs(motion.y()) > 0.1f) && canFallThrough(world.getBlockState(position.below())) && canFallThrough(world.getBlockState(position.below().below()))) {
            entity.fallDistance = 23F;
            entity.setDeltaMovement(motion.x, -3.9, motion.z);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    private boolean canFallThrough(BlockState state) {
        return state.isAir() || state.is(BlockTags.FIRE) || state.liquid() || state.canBeReplaced() || state == BlockRegistry.GOLDEN_CLOUD.get().defaultBlockState();
    }
}
