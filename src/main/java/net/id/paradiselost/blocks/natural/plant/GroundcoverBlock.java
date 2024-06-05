package net.id.paradiselost.blocks.natural.plant;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class GroundcoverBlock extends ParadiseLostBrushBlock {

    public static final VoxelShape SHAPE = Block.createCuboidShape(0, 0, 0, 16, 2, 16);
    private final double slowdown;

    public GroundcoverBlock(Settings settings, double slowdown) {
        super(settings.offset(OffsetType.NONE));
        this.slowdown = slowdown;
    }

    public GroundcoverBlock(Settings settings, TagKey<Block> validFloors, boolean override, double slowdown) {
        super(settings, validFloors, override);
        this.slowdown = slowdown;
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (slowdown < 1 && entity instanceof LivingEntity && entity.getType() != EntityType.FOX && entity.getType() != EntityType.BEE) {
            entity.slowMovement(state, new Vec3d(slowdown, 1, slowdown));
        }
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }
}
